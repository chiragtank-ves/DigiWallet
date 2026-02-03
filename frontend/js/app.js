// Main Application Logic

// ============================================
// UTILITY FUNCTIONS
// ============================================

// Show alert message
function showAlert(message, type = 'success') {
    const alertContainer = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertContainer.appendChild(alert);

    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        alert.remove();
    }, 5000);
}

// Format date
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR'
    }).format(amount || 0);
}

// Mask card number
function maskCardNumber(cardNumber) {
    if (!cardNumber || cardNumber.length < 4) return cardNumber;
    return '**** **** **** ' + cardNumber.slice(-4);
}

// ============================================
// USER MANAGEMENT
// ============================================

// Load all users
async function loadUsers() {
    const tbody = document.getElementById('usersTableBody');
    tbody.innerHTML = '<tr><td colspan="7" class="text-center"><div class="spinner-border text-primary"></div></td></tr>';

    try {
        const users = await API.getAllUsers();

        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center">No users found</td></tr>';
            return;
        }

        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.id}</td>
                <td><strong>${user.username}</strong></td>
                <td>${user.fullName || 'N/A'}</td>
                <td>${user.email || 'N/A'}</td>
                <td><span class="badge ${user.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'}">${user.role}</span></td>
                <td><span class="badge ${user.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">${user.status}</span></td>
                <td>
                    <button class="btn btn-sm btn-info" onclick="viewUserDetails(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning" onclick="toggleUserStatus(${user.id})">
                        <i class="fas fa-sync"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error loading users</td></tr>';
        showAlert('Error loading users: ' + error.message, 'danger');
    }
}

// Create user form handler
document.getElementById('createUserForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userData = {
        username: document.getElementById('username').value,
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        role: document.getElementById('role').value,
        status: document.getElementById('status').value
    };

    try {
        await API.createUser(userData);
        showAlert('User created successfully!', 'success');
        e.target.reset();
        loadUsers();
    } catch (error) {
        showAlert('Error creating user: ' + error.message, 'danger');
    }
});

// View user details
async function viewUserDetails(userId) {
    try {
        const user = await API.getUserById(userId);
        alert(`User Details:\n\nID: ${user.id}\nUsername: ${user.username}\nFull Name: ${user.fullName}\nEmail: ${user.email}\nRole: ${user.role}\nStatus: ${user.status}`);
    } catch (error) {
        showAlert('Error fetching user details: ' + error.message, 'danger');
    }
}

// Toggle user status
async function toggleUserStatus(userId) {
    try {
        await API.updateUserStatus(userId);
        showAlert('User status updated successfully!', 'success');
        loadUsers();
    } catch (error) {
        showAlert('Error updating user status: ' + error.message, 'danger');
    }
}

// ============================================
// WALLET MANAGEMENT
// ============================================

// Create wallet form handler
document.getElementById('createWalletForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userId = document.getElementById('walletUserId').value;
    const initialBalance = document.getElementById('initialBalance').value;

    try {
        // Assuming the wallet is created with user ID
        const walletData = {
            user: { id: userId },
            balance: initialBalance,
            currency: 'INR',
            status: 'ACTIVE'
        };

        await API.createWallet(walletData);
        showAlert('Wallet created successfully!', 'success');
        e.target.reset();
    } catch (error) {
        showAlert('Error creating wallet: ' + error.message, 'danger');
    }
});

// Get wallet form handler
document.getElementById('getWalletForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userId = document.getElementById('getWalletUserId').value;
    const walletDetails = document.getElementById('walletDetails');
    const walletInfo = document.getElementById('walletInfo');

    try {
        const wallet = await API.getWalletByUserId(userId);

        walletInfo.innerHTML = `
            <div class="info-row">
                <span class="info-label">Wallet ID:</span>
                <span class="info-value">${wallet.id}</span>
            </div>
            <div class="info-row">
                <span class="info-label">User ID:</span>
                <span class="info-value">${wallet.user?.id || 'N/A'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Balance:</span>
                <span class="info-value text-success"><strong>${formatCurrency(wallet.balance)}</strong></span>
            </div>
            <div class="info-row">
                <span class="info-label">Currency:</span>
                <span class="info-value">${wallet.currency}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Status:</span>
                <span class="info-value"><span class="badge ${wallet.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">${wallet.status}</span></span>
            </div>
            <div class="info-row">
                <span class="info-label">Created At:</span>
                <span class="info-value">${formatDate(wallet.createdAt)}</span>
            </div>
        `;

        walletDetails.classList.remove('d-none');
        showAlert('Wallet retrieved successfully!', 'success');
    } catch (error) {
        walletDetails.classList.add('d-none');
        showAlert('Error retrieving wallet: ' + error.message, 'danger');
    }
});

// ============================================
// CARD MANAGEMENT
// ============================================

// Create card form handler
document.getElementById('createCardForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const cardData = {
        wallet: { id: document.getElementById('cardWalletId').value },
        cardNumber: document.getElementById('cardNumber').value,
        cardType: document.getElementById('cardType').value,
        expiryDate: document.getElementById('expiryDate').value,
        status: 'ACTIVE'
    };

    try {
        await API.createCard(cardData);
        showAlert('Card created successfully!', 'success');
        e.target.reset();
    } catch (error) {
        showAlert('Error creating card: ' + error.message, 'danger');
    }
});

// Get card form handler
document.getElementById('getCardForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const cardId = document.getElementById('getCardId').value;
    const cardDetails = document.getElementById('cardDetails');
    const cardInfo = document.getElementById('cardInfo');

    try {
        const card = await API.getCardById(cardId);

        cardInfo.innerHTML = `
            <div class="info-row">
                <span class="info-label">Card ID:</span>
                <span class="info-value">${card.id}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Card Number:</span>
                <span class="info-value card-number-masked">${maskCardNumber(card.cardNumber)}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Card Type:</span>
                <span class="info-value"><span class="badge bg-warning text-dark">${card.cardType}</span></span>
            </div>
            <div class="info-row">
                <span class="info-label">Wallet ID:</span>
                <span class="info-value">${card.wallet?.id || 'N/A'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Status:</span>
                <span class="info-value"><span class="badge ${card.status === 'ACTIVE' ? 'bg-success' : 'bg-danger'}">${card.status}</span></span>
            </div>
            <div class="info-row">
                <span class="info-label">Expiry Date:</span>
                <span class="info-value">${card.expiryDate || 'N/A'}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Issued At:</span>
                <span class="info-value">${formatDate(card.issuedAt)}</span>
            </div>
        `;

        cardDetails.classList.remove('d-none');
        showAlert('Card retrieved successfully!', 'success');
    } catch (error) {
        cardDetails.classList.add('d-none');
        showAlert('Error retrieving card: ' + error.message, 'danger');
    }
});

// ============================================
// TRANSACTION MANAGEMENT
// ============================================

// Credit transaction form handler
document.getElementById('creditTransactionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const transactionData = {
        wallet: { id: document.getElementById('creditWalletId').value },
        amount: parseFloat(document.getElementById('creditAmount').value),
        type: 'CREDIT',
        category: document.getElementById('creditCategory').value || 'General',
        status: 'COMPLETED',
        referenceId: 'CR-' + Date.now()
    };

    try {
        await API.createTransaction(transactionData);
        showAlert('Money credited successfully! ₹' + transactionData.amount, 'success');
        e.target.reset();

        // Optionally refresh transactions if a wallet ID is set
        const walletIdField = document.getElementById('transactionWalletId');
        if (walletIdField.value === transactionData.wallet.id.toString()) {
            document.getElementById('getTransactionsForm').dispatchEvent(new Event('submit'));
        }
    } catch (error) {
        showAlert('Error crediting money: ' + error.message, 'danger');
    }
});

// Debit transaction form handler
document.getElementById('debitTransactionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const transactionData = {
        wallet: { id: document.getElementById('debitWalletId').value },
        amount: parseFloat(document.getElementById('debitAmount').value),
        type: 'DEBIT',
        category: document.getElementById('debitCategory').value || 'General',
        status: 'COMPLETED',
        referenceId: 'DB-' + Date.now()
    };

    try {
        await API.createTransaction(transactionData);
        showAlert('Money debited successfully! ₹' + transactionData.amount, 'success');
        e.target.reset();

        // Optionally refresh transactions if a wallet ID is set
        const walletIdField = document.getElementById('transactionWalletId');
        if (walletIdField.value === transactionData.wallet.id.toString()) {
            document.getElementById('getTransactionsForm').dispatchEvent(new Event('submit'));
        }
    } catch (error) {
        showAlert('Error debiting money: ' + error.message, 'danger');
    }
});

// Get transactions form handler
document.getElementById('getTransactionsForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const walletId = document.getElementById('transactionWalletId').value;
    const transactionsContainer = document.getElementById('transactionsContainer');
    const tbody = document.getElementById('transactionsTableBody');

    tbody.innerHTML = '<tr><td colspan="6" class="text-center"><div class="spinner-border text-primary"></div></td></tr>';
    transactionsContainer.classList.remove('d-none');

    try {
        const transactions = await API.getTransactionsByWalletId(walletId);

        if (transactions.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No transactions found</td></tr>';
            return;
        }

        tbody.innerHTML = transactions.map(txn => `
            <tr>
                <td>${txn.id}</td>
                <td>
                    <span class="badge ${txn.type === 'CREDIT' ? 'bg-success' : 'bg-danger'}">
                        ${txn.type}
                    </span>
                </td>
                <td class="${txn.type === 'CREDIT' ? 'transaction-credit' : 'transaction-debit'}">
                    ${txn.type === 'CREDIT' ? '+' : '-'}${formatCurrency(txn.amount)}
                </td>
                <td><span class="badge bg-info">${txn.status || 'COMPLETED'}</span></td>
                <td>${formatDate(txn.transactionDate)}</td>
                <td><small>${txn.referenceId || 'N/A'}</small></td>
            </tr>
        `).join('');

        showAlert('Transactions loaded successfully!', 'success');
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error loading transactions</td></tr>';
        showAlert('Error loading transactions: ' + error.message, 'danger');
    }
});

// ============================================
// INITIALIZATION
// ============================================

// Load users on page load
document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
    console.log('DigiWallet Frontend Initialized');
    console.log('Backend API:', API_BASE_URL);
});

// Smooth scroll for navigation
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const targetId = link.getAttribute('href');
        const targetSection = document.querySelector(targetId);
        if (targetSection) {
            targetSection.scrollIntoView({ behavior: 'smooth' });

            // Update active nav link
            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        }
    });
});
