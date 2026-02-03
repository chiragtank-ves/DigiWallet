# 🚀 DigiWallet - Quick Start Guide

## ⚡ 60-Second Setup

### Prerequisites Check
- [ ] Java 17+ installed
- [ ] Python 3+ installed (for frontend server)
- [ ] MySQL running on localhost:3306
- [ ] Database `digital_wallet_db` created

### Start the Application

**Windows - One-Click Start:**
```bash
# Double-click this file in Windows Explorer
start-all.cmd
```

**Manual Start:**

**Terminal 1 (Backend):**
```bash
./mvnw spring-boot:run
```

**Terminal 2 (Frontend):**
```bash
cd frontend
python -m http.server 3000
```

---

## 🌐 Access URLs

| Service | URL |
|---------|-----|
| **Frontend** | http://localhost:3000 |
| **Backend API** | http://localhost:8080/api |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |

---

## 🎯 First Test

1. **Open Frontend**: http://localhost:3000
2. **Create User**:
   - Username: `test_user`
   - Full Name: `Test User`
   - Role: `USER`
   - Status: `ACTIVE`
3. **Click**: "Create User"
4. **See**: User appears in table below

---

## 📂 Project Structure

```
DigiWallet/
├── 🔧 Backend (Spring Boot - Port 8080)
│   ├── src/main/java/...       # Java code
│   └── src/main/resources/     # Configuration
│
├── 🎨 Frontend (Express - Port 3000)
│   ├── public/
│   │   ├── index.html          # UI
│   │   ├── css/style.css       # Styles
│   │   └── js/                 # JavaScript
│   └── server.js               # Express server
│
└── 📝 Documentation
    ├── SETUP_GUIDE.md           # Full setup
    ├── copilot-instructions.md  # Dev guidelines
    └── README.md                # Project overview
```

---

## 🔧 Key Features

### ✨ What You Can Do

| Feature | Description |
|---------|-------------|
| 👥 **User Management** | Create, view, update users |
| 💼 **Wallet Operations** | Create wallets, check balance |
| 💳 **Card Management** | Add payment cards to wallets |
| 📊 **Transactions** | View transaction history |

---

## 🛠️ Tech Stack

**Backend:**
- Spring Boot 3.5.9
- Java 17
- MySQL 8.0
- JPA/Hibernate

**Frontend:**
- HTML5 + CSS3
- Bootstrap 5.3
- Vanilla JavaScript (ES6+)
- Python HTTP Server (no Node.js!)

---

## 🐛 Troubleshooting

### Backend Won't Start
```bash
# Check if MySQL is running
# Check application.properties for correct DB credentials
# Verify Java 17+ is installed: java -version
```

### Frontend Won't Start
```bash
# Check if Python is installed: python --version
# Port 3000 is already in use? Change port in start.cmd
```

### CORS Errors
```bash
# Ensure WebConfig.java exists in src/main/java/com/orion/DigiWallet/config/
# Restart backend server
```

### "Connection Refused"
```bash
# Backend must be running BEFORE you use the frontend
# Check backend at: http://localhost:8080/swagger-ui.html
```

---

## 📖 Learn More

- **Full Setup Guide**: `SETUP_GUIDE.md`
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Frontend Details**: `frontend/README.md`
- **Backend TODOs**: `TODO_MASTER_LIST.md`

---

## 🎓 Tutorial

### Create Your First User-Wallet-Card Flow

**Step 1: Create User**
```
Frontend → User Management → Fill Form → Create User
Note the User ID (e.g., 1)
```

**Step 2: Create Wallet**
```
Frontend → Wallet Management → Enter User ID: 1 → Create Wallet
Note the Wallet ID (e.g., 1)
```

**Step 3: Create Card**
```
Frontend → Card Management → Enter Wallet ID: 1
Card Number: 1234567890123456
Card Type: DEBIT
→ Create Card
```

**Step 4: View Everything**
```
Check users table
Get wallet by User ID
Get card by Card ID
View transactions (if any)
```

---

## 💡 Pro Tips

1. **Use Swagger UI** for API testing: http://localhost:8080/swagger-ui.html
2. **Check Browser Console** (F12) for frontend errors
3. **Check Backend Logs** in terminal for API errors
4. **Use Chrome DevTools Network Tab** to see API calls
5. **Start Backend First**, then Frontend

---

## 🎯 Next Steps

1. ✅ Start the application
2. ✅ Create a test user
3. ✅ Create a wallet
4. ✅ Create a card
5. ✅ Explore the UI
6. 📚 Read full documentation
7. 🔧 Customize and extend

---

## 🚨 Important Notes

- **Development Mode**: Not secure, for learning only
- **CORS Enabled**: Frontend can call backend
- **No Auth**: All endpoints are public
- **Auto-Reload**: Frontend auto-reloads on changes

---

## 📞 Need Help?

1. **Check Logs**: Backend terminal & Browser console
2. **Read Docs**: SETUP_GUIDE.md has detailed troubleshooting
3. **Verify Setup**: All prerequisites installed?
4. **Test Backend**: http://localhost:8080/swagger-ui.html

---

**🎉 You're Ready to Go!**

Run `start-all.cmd` and open http://localhost:3000

Happy Coding! 👨‍💻👩‍💻
