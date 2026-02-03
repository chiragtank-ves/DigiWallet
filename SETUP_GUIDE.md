# DigiWallet - Complete Setup Guide

## 📁 Project Structure

```
DigiWallet/
├── src/                          # Backend (Spring Boot)
│   ├── main/
│   │   ├── java/
│   │   │   └── com/orion/DigiWallet/
│   │   │       ├── config/       # CORS & other configs
│   │   │       ├── controller/   # REST Controllers
│   │   │       ├── service/      # Business logic
│   │   │       ├── repository/   # Data access
│   │   │       └── model/        # JPA Entities
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Backend tests
├── frontend/                     # Frontend (HTML/CSS/JS)
│   ├── public/
│   │   ├── index.html           # Main UI
│   │   ├── css/
│   │   │   └── style.css        # Custom styles
│   │   └── js/
│   │       ├── api.js           # API service
│   │       └── app.js           # Application logic
│   ├── server.js                # Express server
│   ├── package.json             # Dependencies
│   └── README.md                # Frontend docs
├── pom.xml                      # Maven configuration
├── start-all.cmd                # Start both servers
└── copilot-instructions.md      # Project guidelines
```

---

## 🚀 Quick Start

### Option 1: Start Both Servers at Once (Windows)

Double-click `start-all.cmd` in the root directory

This will:
1. Start the backend server on port 8080
2. Start the frontend server on port 3000
3. Open both in separate command windows

### Option 2: Start Manually

**Terminal 1 - Backend:**
```bash
cd DigiWallet
./mvnw spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd DigiWallet/frontend
npm install  # First time only
npm start
```

---

## 📋 Prerequisites

### Backend Requirements:
- ✅ Java 17 or higher
- ✅ Maven (included via mvnw)
- ✅ MySQL 8.0 running on localhost:3306
- ✅ Database: `digital_wallet_db`

### Frontend Requirements:
- ✅ Node.js 14+ (with npm)

---

## 🔧 Setup Instructions

### Step 1: Database Setup

1. **Start MySQL Server**
2. **Create Database:**
   ```sql
   CREATE DATABASE digital_wallet_db;
   ```

3. **Run initialization script** (if available):
   ```bash
   cd dbscript
   mysql -u root -p < db.sql
   ```

### Step 2: Backend Configuration

1. **Verify `application.properties`:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/digital_wallet_db
   spring.datasource.username=root
   spring.datasource.password=n3u3da!
   ```

2. **Update password if needed**

### Step 3: Frontend Installation

```bash
cd frontend
npm install
```

This installs:
- express (web server)
- cors (cross-origin support)
- nodemon (dev auto-reload)

### Step 4: Start the Application

**Method A - Use startup script (Windows):**
```bash
start-all.cmd
```

**Method B - Manual start:**

Terminal 1 (Backend):
```bash
./mvnw spring-boot:run
```

Terminal 2 (Frontend):
```bash
cd frontend
npm start
```

---

## 🌐 Access Points

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend UI** | http://localhost:3000 | Main application interface |
| **Backend API** | http://localhost:8080/api | REST API endpoints |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API documentation |

---

## 🎯 Testing the Application

### 1. Create a User

**Frontend Method:**
1. Go to http://localhost:3000
2. Fill in User Management form:
   - Username: `john_doe`
   - Full Name: `John Doe`
   - Email: `john@example.com`
   - Role: `USER`
   - Status: `ACTIVE`
3. Click "Create User"

**API Method (Swagger):**
1. Go to http://localhost:8080/swagger-ui.html
2. POST `/api/users`
3. Use this JSON:
   ```json
   {
     "username": "john_doe",
     "fullName": "John Doe",
     "email": "john@example.com",
     "role": "USER",
     "status": "ACTIVE"
   }
   ```

### 2. Create a Wallet

**Frontend:**
1. Navigate to "Wallet Management"
2. Enter User ID (from step 1)
3. Set initial balance: 1000
4. Click "Create Wallet"

### 3. Create a Card

**Frontend:**
1. Navigate to "Card Management"
2. Enter Wallet ID
3. Card Number: 1234567890123456
4. Card Type: DEBIT
5. Click "Create Card"

### 4. View Transactions

**Frontend:**
1. Navigate to "Transaction Management"
2. Enter Wallet ID
3. Click "Get Transactions"

---

## 🔍 Troubleshooting

### Backend Issues

**Port 8080 already in use:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <process_id> /F
```

**Database connection failed:**
- Verify MySQL is running
- Check username/password in application.properties
- Ensure database exists: `digital_wallet_db`

**JPA Entity not found:**
- Check all entities have `@Entity` annotation
- Verify package scanning in main class

### Frontend Issues

**Port 3000 already in use:**
- Change port in `server.js`:
  ```javascript
  const PORT = 3001; // Change this
  ```

**CORS errors:**
- Ensure backend `WebConfig.java` exists
- Restart backend after adding CORS config
- Check browser console for specific error

**API calls failing:**
- Verify backend is running on port 8080
- Check `api.js` API_BASE_URL
- Open DevTools Network tab to see errors

**Dependencies not installing:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

## 📦 Key Files Explained

### Backend Files

**`WebConfig.java`** (CORS Configuration)
- Allows frontend (port 3000) to call backend API (port 8080)
- Required for cross-origin requests

**`UserController.java`**
- REST endpoints for user management
- Returns JSON responses

**`UserService.java`**
- Business logic layer
- Validates and processes requests

**`UserRepository.java`**
- Data access layer
- Extends JpaRepository

### Frontend Files

**`index.html`**
- Main UI structure
- Bootstrap 5 for responsive design
- Font Awesome icons

**`style.css`**
- Custom styling
- Animations and transitions
- Responsive breakpoints

**`api.js`**
- Centralized API service
- Handles all HTTP requests
- Error handling

**`app.js`**
- Application logic
- Event handlers
- DOM manipulation
- Data formatting

**`server.js`**
- Express web server
- Serves static files
- Runs on port 3000

---

## 🔐 Security Notes

### Current Setup (Development):
- CORS allows localhost:3000
- No authentication/authorization
- Plain HTTP (not HTTPS)

### For Production:
- [ ] Add Spring Security
- [ ] Implement JWT authentication
- [ ] Enable HTTPS
- [ ] Restrict CORS to specific domain
- [ ] Add input validation
- [ ] Implement rate limiting
- [ ] Add API keys
- [ ] Set up environment variables

---

## 🎨 Customization

### Change Backend Port

**application.properties:**
```properties
server.port=8081
```

**frontend/public/js/api.js:**
```javascript
const API_BASE_URL = 'http://localhost:8081/api';
```

### Change Frontend Port

**frontend/server.js:**
```javascript
const PORT = 3001;
```

### Modify UI Colors

**frontend/public/css/style.css:**
```css
:root {
    --primary-color: #your-color;
    --success-color: #your-color;
    /* etc... */
}
```

---

## 📊 API Endpoints Reference

### Users
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user status
- `DELETE /api/users/{id}` - Delete user

### Wallets
- `GET /api/wallets/user/{userId}` - Get wallet by user
- `GET /api/wallets/{id}` - Get wallet by ID
- `POST /api/wallets` - Create wallet

### Cards
- `GET /api/cards/{id}` - Get card by ID
- `POST /api/cards/create` - Create card
- `PUT /api/cards/{id}` - Update card
- `DELETE /api/cards/{id}` - Delete card

### Transactions
- `GET /api/transactions/wallet/{walletId}` - Get transactions
- `POST /api/transactions` - Create transaction
- `GET /api/transactions/{id}` - Get transaction by ID

---

## 🧪 Testing

### Backend Tests
```bash
./mvnw test
```

### Frontend Tests
Currently manual testing via UI. Consider adding:
- Jest for unit tests
- Cypress for E2E tests

---

## 📚 Additional Resources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Bootstrap Docs**: https://getbootstrap.com/docs/
- **Express.js Docs**: https://expressjs.com/
- **MySQL Docs**: https://dev.mysql.com/doc/

---

## 🐛 Known Issues

1. **No authentication**: All endpoints are publicly accessible
2. **No data validation**: Frontend doesn't validate all inputs
3. **Error messages**: Some errors could be more user-friendly
4. **No loading states**: Some operations don't show loading indicators

---

## 🚀 Future Enhancements

- [ ] User authentication (login/logout)
- [ ] Role-based access control
- [ ] Transaction creation (credit/debit)
- [ ] Export data to CSV/PDF
- [ ] Dark mode toggle
- [ ] Real-time updates (WebSocket)
- [ ] Mobile app version
- [ ] Docker containerization

---

## 📝 Development Workflow

1. **Start both servers**
2. **Make changes to code**
3. **Backend**: Restart Spring Boot (`Ctrl+C` then rerun)
4. **Frontend**: Auto-reloads if using `npm run dev`
5. **Test in browser**
6. **Commit changes**

---

## 🤝 Support

For issues or questions:
1. Check browser console (F12)
2. Check backend logs
3. Review this guide
4. Check TODO_MASTER_LIST.md

---

**Happy Coding! 🎉**

**Last Updated**: February 2026
**Version**: 1.0.0
