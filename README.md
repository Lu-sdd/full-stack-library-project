# Library Management System

A full-stack web application that provides a complete digital library solution, enabling users to search, borrow, and manage books online while providing librarians with comprehensive management tools.

## 🚀 Features

- **Book Management**: Search, browse, and filter books by category (Frontend, Backend, Data, DevOps)
- **User Authentication**: Secure login system using Okta OAuth2 integration
- **Book Checkout System**: Borrow and return books with automatic inventory tracking
- **Payment Processing**: Integrated Stripe payment system for library fees
- **Review System**: Leave ratings and reviews for books
- **User Dashboard**: Personal bookshelf showing current loans, reading history, and messages
- **Responsive Design**: Mobile-friendly interface built with Bootstrap

## 🛠️ Tech Stack

### Backend
- **Spring Boot 3.0.7** - Java 17 framework
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication and authorization
- **MySQL** - Database
- **Okta OAuth2** - Identity management
- **Stripe** - Payment processing
- **Lombok** - Code generation

### Frontend
- **React 18** - UI library
- **TypeScript** - Type-safe JavaScript
- **React Router** - Client-side routing
- **Bootstrap** - Responsive CSS framework
- **Okta React** - Authentication components
- **Stripe React** - Payment components

## 📱 Screenshots

### Homepage
![Homepage](screenshots/homepage.png)
*Main landing page with featured books and library services*

### Book Search
![Book Search](screenshots/book-search.png)
*Search interface with category filtering and pagination*

### User Dashboard
![User Dashboard](screenshots/user-dashboard.png)
*Personal bookshelf showing current loans and reading history*

### Book Checkout
![Book Checkout](screenshots/book-checkout.png)
*Book details page with checkout functionality and reviews*

### Payment Page
![Payment Page](screenshots/payment-page.png)
*Stripe-integrated payment interface for library fees*

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Okta developer account
- Stripe account

### Backend Setup
1. Clone the repository
2. Navigate to `backend/spring-boot-library/`
3. Configure `application.properties` with your database and Okta credentials
4. Run `./mvnw spring-boot:run`

### Frontend Setup
1. Navigate to `frontend/react-library/`
2. Install dependencies: `npm install`
3. Configure Okta settings in `src/lib/oktaConfig.ts`
4. Start the development server: `npm start`

## 📁 Project Structure

### Backend Architecture (Spring Boot)
```
backend/spring-boot-library/
├── src/main/java/com/luv2code/springbootlibrary/
│   ├── config/                          # Configuration classes
│   │   ├── SecurityConfiguration.java   # Spring Security setup
│   │   └── MyDataRestConfig.java       # Data REST configuration
│   ├── Controller/                      # REST API endpoints
│   │   ├── BookController.java         # Book management APIs
│   │   ├── ReviewController.java       # Review management APIs
│   │   ├── PaymentController.java      # Payment processing APIs
│   │   └── MessagesController.java     # User messaging APIs
│   ├── entity/                          # JPA entities
│   │   ├── Book.java                   # Book data model
│   │   ├── Checkout.java               # Book checkout model
│   │   ├── Review.java                 # Book review model
│   │   ├── Payment.java                # Payment model
│   │   ├── Message.java                # User message model
│   │   └── History.java                # Reading history model
│   ├── dao/                             # Data access layer
│   │   ├── BookRepository.java         # Book data operations
│   │   ├── CheckoutRepository.java     # Checkout data operations
│   │   ├── ReviewRepository.java       # Review data operations
│   │   └── PaymentRepository.java      # Payment data operations
│   ├── service/                         # Business logic layer
│   │   ├── BookService.java            # Book business operations
│   │   ├── ReviewService.java          # Review business operations
│   │   ├── PaymentService.java         # Payment business operations
│   │   └── MessageService.java         # Message business operations
│   ├── requestmodels/                   # Request DTOs
│   │   ├── ReviewRequest.java          # Review creation request
│   │   └── PaymentInfoRequest.java     # Payment request
│   ├── responsemodels/                  # Response DTOs
│   │   └── ShelfCurrentLoansResponse.java # User loans response
│   └── utils/                           # Utility classes
│       └── ExtractJWT.java             # JWT token extraction
```

### Frontend Architecture (React + TypeScript)
```
frontend/react-library/
├── src/
│   ├── layouts/                         # Page layouts and components
│   │   ├── HomePage/                    # Homepage components
│   │   │   ├── HomePage.tsx            # Main homepage
│   │   │   └── components/             # Homepage sub-components
│   │   │       ├── Carousel.tsx        # Featured books carousel
│   │   │       ├── ExploreTopBooks.tsx # Top books section
│   │   │       ├── Heros.tsx           # Hero section
│   │   │       └── LibraryServices.tsx # Services overview
│   │   ├── SearchBooksPage/            # Book search functionality
│   │   │   ├── SearchBooksPage.tsx     # Main search page
│   │   │   └── components/
│   │   │       └── SearchBook.tsx      # Individual book display
│   │   ├── BookCheckoutPage/           # Book checkout and reviews
│   │   │   ├── BookCheckoutPage.tsx    # Main checkout page
│   │   │   ├── CheckoutAndReviewBox.tsx # Checkout functionality
│   │   │   ├── LatestReviews.tsx       # Recent reviews display
│   │   │   └── ReviewListPage/         # Full review listing
│   │   ├── ShelfPage/                  # User personal shelf
│   │   │   ├── ShelfPage.tsx           # Main shelf page
│   │   │   └── components/
│   │   │       ├── Loans.tsx           # Current loans display
│   │   │       ├── HistoryPage.tsx     # Reading history
│   │   │       └── LoansModal.tsx      # Loan details modal
│   │   ├── MessagesPage/               # User messaging system
│   │   │   ├── MessagesPage.tsx        # Main messages page
│   │   │   └── components/
│   │   │       ├── Messages.tsx        # Message display
│   │   │       └── PostNewMessage.tsx  # New message creation
│   │   ├── PaymentPage/                # Payment processing
│   │   │   └── PaymentPage.tsx         # Stripe payment interface
│   │   └── NavbarAndFooter/            # Navigation components
│   │       ├── Navbar.tsx              # Main navigation bar
│   │       └── Footer.tsx              # Page footer
│   ├── Auth/                           # Authentication components
│   │   ├── LoginWidget.jsx             # Login interface
│   │   └── OktaSignInWidget.jsx        # Okta integration
│   ├── models/                         # TypeScript interfaces
│   │   ├── BookModel.ts                # Book data interface
│   │   ├── ReviewModel.ts              # Review data interface
│   │   ├── PaymentInfoRequest.ts       # Payment request interface
│   │   └── ShelfCurrentLoans.ts       # User loans interface
│   ├── Utils/                          # Reusable components
│   │   ├── SpinnerLoading.tsx          # Loading spinner
│   │   ├── Pagination.tsx              # Page navigation
│   │   ├── StarsReview.tsx             # Star rating component
│   │   └── LeaveAReview.tsx            # Review creation form
│   ├── lib/                            # Configuration and utilities
│   │   └── oktaConfig.ts               # Okta authentication config
│   ├── App.tsx                         # Main application component
│   └── index.tsx                       # Application entry point
```

## 🌟 Key Features Explained

### Authentication & Security
- OAuth2 integration with Okta for enterprise-grade authentication
- JWT token validation for secure API access
- Role-based access control for different user types
- CORS configuration for secure cross-origin requests

### Database Design
- Normalized database schema with proper relationships
- JPA entities for Book, User, Checkout, Review, and Message
- Automatic inventory tracking for book copies
- Transaction management for checkout/return operations

### User Experience
- Real-time search with category filtering
- Responsive design for all device sizes
- Intuitive navigation and user interface
- Comprehensive error handling and user feedback

## 🤝 Contributing

This project was developed as a learning exercise and portfolio piece. Feel free to fork the repository and experiment with the code.

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

Developed as a full-stack project to demonstrate proficiency in modern web development technologies and best practices.

---

**Note**: This is a demonstration project. For production use, additional security measures, testing, and deployment configurations would be required.