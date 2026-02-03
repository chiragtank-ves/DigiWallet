# DigiWallet Frontend

A simple and elegant frontend application for the DigiWallet Digital Wallet Management System.

## 🚀 Features

- **User Management**: Create, view, and manage users
- **Wallet Operations**: Create wallets and view wallet details
- **Card Management**: Create and manage payment cards
- **Transaction Operations**: Credit/Debit money and view transaction history
- **Responsive Design**: Works on desktop and mobile devices
- **Real-time Updates**: Dynamic data loading and updates

## 🛠️ Technology Stack

- **HTML5**: Semantic markup
- **CSS3**: Custom styles with animations
- **Bootstrap 5.3**: Responsive UI framework
- **JavaScript (ES6+)**: Pure vanilla JavaScript (no frameworks)
- **Font Awesome**: Icons
- **Python HTTP Server**: Lightweight web server (no Node.js required!)

## 📋 Prerequisites

- Python 3.x (for running the HTTP server)
- DigiWallet Backend running on `http://localhost:8080`

## 🔧 Installation

**No installation required!** This is a pure HTML/CSS/JavaScript application.

Simply ensure you have Python 3 installed:
```bash
python --version
```

## 🎯 Running the Application

### Development Mode:
```bash
cd frontend
python -m http.server 3000
```

Or simply double-click:
```bash
start.cmd
```

The frontend server will start on **http://localhost:3000**

## 📁 Project Structure

```
frontend/
├── index.html          # Main HTML file
├── css/
│   └── style.css       # Custom styles
├── js/
│   ├── api.js          # API service layer
│   └── app.js          # Main application logic
├── start.cmd           # Windows startup script
└── README.md          # This file
```

## 🔌 API Integration

The frontend communicates with the backend API at `http://localhost:8080/api`

### API Endpoints Used:

#### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user status

#### Wallets
- `GET /api/wallets/user/{userId}` - Get wallet by user ID
- `POST /api/wallets` - Create new wallet

#### Cards
- `GET /api/cards/{id}` - Get card by ID
- `POST /api/cards/create` - Create new card

#### Transactions
- `GET /api/transactions/wallet/{walletId}` - Get transactions by wallet

## 🎨 UI Components

### User Management
- Create new users with username, full name, email, role, and status
- View all users in a data table
- Toggle user status
- View user details

### Wallet Management
- Create wallet for a user
- View wallet details including balance and status
- Currency formatting (INR)

### Card Management
- Create payment cards (Debit, Credit, Prepaid, Virtual)
- View card details with masked card numbers
- Card status tracking

### Transaction History
- View all transactions for a wallet
- Color-coded transaction types (Credit/Debit)
- Transaction amount formatting
- Date and reference ID display

## 🎯 Usage Guide

### 1. Start Backend Server
Ensure the DigiWallet backend is running on port 8080:
```bash
cd DigiWallet
./mvnw spring-boot:run
```

### 2. Start Frontend Server
In a new terminal:
```bash
cd frontend
npm start
```

### 3. Access the Application
Open your browser and navigate to:
```
http://localhost:3000
```

### 4. Create Test Data

**Step 1: Create a User**
- Navigate to User Management section
- Fill in the form:
  - Username: `john_doe`
  - Full Name: `John Doe`
  - Email: `john@example.com`
  - Role: `USER`
  - Status: `ACTIVE`
- Click "Create User"

**Step 2: Create a Wallet**
- Navigate to Wallet Management section
- Enter the User ID (from step 1)
- Set initial balance (optional)
- Click "Create Wallet"

**Step 3: Create a Card**
- Navigate to Card Management section
- Enter Wallet ID
- Enter 16-digit card number
- Select card type
- Click "Create Card"

**Step 4: View Transactions**
- Navigate to Transaction Management
- Enter Wallet ID
- Click "Get Transactions"

## 🔒 CORS Configuration

The frontend runs on a different port (3000) than the backend (8080). Make sure CORS is enabled in your Spring Boot backend:

Add to your Spring Boot application:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
```

## 🎨 Customization

### Change API Base URL
Edit `public/js/api.js`:
```javascript
const API_BASE_URL = 'http://your-backend-url:port/api';
```

### Modify Styles
Edit `public/css/style.css` to customize colors, fonts, and layouts.

### Add New Features
1. Add HTML structure in `public/index.html`
2. Create API methods in `public/js/api.js`
3. Implement UI logic in `public/js/app.js`

## 🐛 Troubleshooting

### Backend Connection Issues
- Verify backend is running: `http://localhost:8080/swagger-ui.html`
- Check CORS configuration
- Verify API endpoints in browser console

### Frontend Not Loading
- Check if port 3000 is available
- Clear browser cache
- Check browser console for errors

### Data Not Displaying
- Check browser console for API errors
- Verify backend returns correct data format
- Check network tab in browser DevTools

## 📝 Development Notes

- All API calls are asynchronous (async/await)
- Error handling included for all API operations
- Alert system for user feedback
- Loading states for async operations
- Form validation included

## 🚀 Deployment

For production deployment:

1. Build optimized version (if using build tools)
2. Configure production API URL
3. Enable HTTPS
4. Set up proper CORS rules
5. Use environment variables for configuration

## 📄 License

This project is part of the DigiWallet learning project.

## 🤝 Contributing

This is a learning project. Feel free to:
- Add new features
- Improve UI/UX
- Fix bugs
- Add more API integrations

## 📞 Support

For issues or questions:
- Check browser console for errors
- Verify backend is running
- Check API documentation at `http://localhost:8080/swagger-ui.html`

---

**Happy Coding! 🎉**
