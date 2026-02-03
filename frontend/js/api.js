// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Service Object
const API = {
    // Base fetch wrapper with error handling
    async fetchAPI(endpoint, options = {}) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });

            // Check if response is OK
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || `HTTP error! status: ${response.status}`);
            }

            // Check if response has content
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return null; // For responses with no content (e.g., DELETE)
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    // ============================================
    // USER ENDPOINTS
    // ============================================

    // Get all users
    async getAllUsers() {
        return this.fetchAPI('/users');
    },

    // Get user by ID
    async getUserById(id) {
        return this.fetchAPI(`/users/${id}`);
    },

    // Create new user
    async createUser(userData) {
        return this.fetchAPI('/users', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    },

    // Update user status
    async updateUserStatus(id) {
        return this.fetchAPI(`/users/${id}`, {
            method: 'PUT'
        });
    },

    // Delete user
    async deleteUser(id) {
        return this.fetchAPI(`/users/${id}`, {
            method: 'DELETE'
        });
    },

    // ============================================
    // WALLET ENDPOINTS
    // ============================================

    // Get wallet by user ID
    async getWalletByUserId(userId) {
        return this.fetchAPI(`/wallets/user/${userId}`);
    },

    // Get wallet by ID
    async getWalletById(id) {
        return this.fetchAPI(`/wallets/${id}`);
    },

    // Create wallet (if endpoint exists)
    async createWallet(walletData) {
        return this.fetchAPI('/wallets', {
            method: 'POST',
            body: JSON.stringify(walletData)
        });
    },

    // ============================================
    // CARD ENDPOINTS
    // ============================================

    // Get card by ID
    async getCardById(id) {
        return this.fetchAPI(`/cards/${id}`);
    },

    // Create new card
    async createCard(cardData) {
        return this.fetchAPI('/cards/create', {
            method: 'POST',
            body: JSON.stringify(cardData)
        });
    },

    // Update card
    async updateCard(id, cardData) {
        return this.fetchAPI(`/cards/${id}`, {
            method: 'PUT',
            body: JSON.stringify(cardData)
        });
    },

    // Delete card
    async deleteCard(id) {
        return this.fetchAPI(`/cards/${id}`, {
            method: 'DELETE'
        });
    },

    // ============================================
    // TRANSACTION ENDPOINTS
    // ============================================

    // Get transactions by wallet ID
    async getTransactionsByWalletId(walletId) {
        return this.fetchAPI(`/transactions/wallet/${walletId}`);
    },

    // Create transaction (Credit/Debit)
    async createTransaction(transactionData) {
        return this.fetchAPI('/transactions', {
            method: 'POST',
            body: JSON.stringify(transactionData)
        });
    },

    // Get transaction by ID
    async getTransactionById(id) {
        return this.fetchAPI(`/transactions/${id}`);
    }
};

// Export API object (if using modules)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = API;
}
