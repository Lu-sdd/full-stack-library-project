package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.dao.HistoryRepository;
import com.luv2code.springbootlibrary.dao.PaymentRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.entity.History;
import com.luv2code.springbootlibrary.entity.Payment;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository; //声明这些变量的目的是为了在service类中能够调用这些repository的方法，从而实现对数据库的操作
    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    private PaymentRepository paymentRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository, PaymentRepository paymentRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository  = paymentRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{
        // 尝试通过bookId找到图书
        Optional<Book> book = bookRepository.findById(bookId);
        // 检查当前用户是否已经借阅了这本书
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        // 如果图书不存在，或者用户已经借阅了这本书，或者书的可用副本数为0或更少，则抛出异常
        if(!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        // 查询当前用户已借阅的所有图书记录
        List<Checkout> currentBooksCheckedOut = checkoutRepository.findBooksByUserEmail(userEmail);
        // 创建一个SimpleDateFormat对象，用于解析和格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 初始化一个标志变量，用来标记是否有图书需要归还
        boolean bookNeedsReturned = false;
        // 遍历用户当前借阅的所有图书
        for (Checkout checkout: currentBooksCheckedOut) {
            // 将借阅记录中的应还日期字符串转换为Date对象
            Date d1 = sdf.parse(checkout.getReturnDate());
            // 将当前日期转换为Date对象
            Date d2 = sdf.parse(LocalDate.now().toString());
            // 使用TimeUnit枚举来处理时间单位转换
            TimeUnit time = TimeUnit.DAYS;

            // 计算应还日期与当前日期之间的差异（天数）
            // Date.getTime()方法返回的是自1970年1月1日00:00:00 GMT以来的毫秒数。
            double differenceInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

            // 如果差异小于0，说明至少有一本书已超过还书日期
            if (differenceInTime < 0){
                bookNeedsReturned = true;
                break; // 退出循环
            }
        }

        // 查询用户的支付记录
        Payment userPayment = paymentRepository.findByUserEmail(userEmail);

        // 如果用户有未支付的金额 或者 有图书超期未还，则不允许用户借书
        if ((userPayment != null && userPayment.getAmount()>0) || (userPayment != null && bookNeedsReturned)){
            throw new Exception("Outstanding fees");
        }

        // 如果用户没有支付记录，则创建一个新的支付记录并设置金额为0
        if (userPayment == null){
            Payment payment = new Payment();
            payment.setAmount(00.00); // 初始化金额为0
            payment.setUserEmail(userEmail); // 设置用户邮箱
            paymentRepository.save(payment); // 保存支付记录到数据库
        }

        // 减少图书的可用副本数
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        // 更新图书的副本数信息到数据库
        bookRepository.save(book.get());

        // 创建一个新的Checkout实例，记录借书的用户、借出日期和应还日期
        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        // 保存借书记录到数据库
        checkoutRepository.save(checkout);

        // 返回被借出的图书信息
        return book.get();
    }

    public boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateCheckout != null){
            return true;
        }else{
            return false;
        }
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        //Initialize a list to store responses
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        // Retrieve a list of Checkouts associated with the given user's email
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        //Create a list to store book IDs from checkouts
        List<Long> bookIdList = new ArrayList<>();

        //Extract book IDs from checkouts and add them to bookIdList
        for (Checkout i: checkoutList){
            bookIdList.add(i.getBookId());
        }

        //Retrieve book information based on the collected book ids
        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        //Create a date formatter for parsing dates. It can parse the date string which satisfies pattern here and return a Date object.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Find the corresponding checkout record for the book.
        for(Book book : books){
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId()).findFirst();

            // If a checkout record exists:
            if (checkout.isPresent()){

                // Parse the return date and the current date.
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                // Define the TimeUnit as DAYS；TimeUnit is an enum in Java that represents various units of time, such as days, hours, minutes, and seconds.
                TimeUnit time = TimeUnit.DAYS;

                //Calculate the time difference in days between d1 and d2
                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book,(int)difference_In_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }

    //Return book 还书
    public void returnBook (String userEmail, Long bookId) throws Exception{

        // 尝试通过ID查找图书，结果被包装在Optional中以处理空值情况。"Optional"的设计是为了避免空指针异常问题。其可以将可能为空的对象包装起来；get（）方法就是为了获得其包裹的对象的具体值
        Optional<Book> book = bookRepository.findById(bookId);
        // 尝试根据用户邮箱和图书ID查找当前的借阅记录
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        // 如果找不到图书或该用户没有借阅这本书，则抛出"图书不存在或者用户并没有借阅这本书"
        if(book.isEmpty() || validateCheckout == null ){
            throw new Exception("Book does not exist or not checked out by user");
        }

        // 图书找到后，增加可用副本数
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);

        // 更新图书信息到数据库
        bookRepository.save(book.get());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validateCheckout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        TimeUnit time = TimeUnit.DAYS;
        double differenceInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

        //在还书时检查是否逾期，如果逾期则需要添加欠费信息
        if(differenceInTime < 0){
            Payment payment = paymentRepository.findByUserEmail(userEmail);

            payment.setAmount(payment.getAmount() + differenceInTime * -1);
            paymentRepository.save(payment);
        }

        // 删除用户的借阅记录
        checkoutRepository.deleteById(validateCheckout.getId());

        //在还书之后，创建并保存history
        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );
        // 保存归还历史到数据库
        historyRepository.save(history);
    }
}
