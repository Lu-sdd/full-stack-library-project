package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
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

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);

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
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(book.isEmpty() || validateCheckout == null ){
            throw new Exception("Book does not exist or not checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());
    }
}
