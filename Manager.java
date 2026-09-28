import java.sql.*;

public class Manager {

    public boolean isUsernameValid(String username) {  // Returns true if username is valid
        return !username.isEmpty() && username.trim().equals(username);
    }

    public boolean isPasswordValid(String password) {  // Returns true if password is valid
        return !password.isEmpty() && password.trim().equals(password);
    }

    public int getNextUserId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextUserId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(user_id) FROM users";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextUserId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next user id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextUserId;
    }

    public int getNextAddressId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextAddressId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(address_id) FROM addresses";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextAddressId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next address id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextAddressId;
    }

        public int getNextProductId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextProductId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(product_id) FROM products";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextProductId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next product id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextProductId;
    }

    public int getNextCartItemId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextCartItemId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(cart_item_id) FROM shopping_cart_items";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextCartItemId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next cart item id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextCartItemId;
    }

    public int getNextOrderId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextOrderId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(order_id) FROM orders";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextOrderId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next order id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextOrderId;
    }

    public int getNextOrderItemId() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        int nextOrderItemId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT MAX(order_item_id) FROM order_items";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nextOrderItemId = rs.getInt(1) + 1;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting next order item id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return nextOrderItemId;
    }

    public boolean addBuyerToDB(String username, String password, Address address) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();

            int userId = getNextUserId();
            int addressId = getNextAddressId();

            String sql1 = "INSERT INTO addresses(address_id, state_name, city_name, street_name, building_num) " +
                        "VALUES (?, ?, ?, ?, ?)";

            pstmt1 = conn.prepareStatement(sql1);

            pstmt1.setInt(1, addressId);
            pstmt1.setString(2, address.getStateName());
            pstmt1.setString(3, address.getCityName());
            pstmt1.setString(4, address.getStreetName());
            pstmt1.setInt(5, address.getBuildingNum());

            pstmt1.executeUpdate();

            String sql2 = "INSERT INTO users(user_id, username, password, user_type) " +
            "VALUES (?, ?, ?, ?)";

            pstmt2 = conn.prepareStatement(sql2);

            pstmt2.setInt(1, userId);
            pstmt2.setString(2, username);
            pstmt2.setString(3, password);
            pstmt2.setString(4, "BUYER");

            pstmt2.executeUpdate();

            String sql3 = "INSERT INTO buyers(buyer_id, address_id) " +
            "VALUES (?, ?)";

            pstmt3 = conn.prepareStatement(sql3);

            pstmt3.setInt(1, userId);
            pstmt3.setInt(2, addressId);

            pstmt3.executeUpdate();

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while adding buyer.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(null, pstmt1, null);
            closeDB(null, pstmt2, null);
            closeDB(null, pstmt3, conn);
        }

        return success;
    }

    public boolean isUsernameTakenDB(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean isTaken = false;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                isTaken = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking username.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return isTaken;
    }

    public boolean addSellerToDB(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();

            int userId = getNextUserId();

            String sql1 = "INSERT INTO users(user_id, username, password, user_type) " +
            "VALUES (?, ?, ?, ?)";

            pstmt1 = conn.prepareStatement(sql1);

            pstmt1.setInt(1, userId);
            pstmt1.setString(2, username);
            pstmt1.setString(3, password);
            pstmt1.setString(4, "SELLER");

            pstmt1.executeUpdate();

            String sql2 = "INSERT INTO sellers(seller_id) " +
            "VALUES (?)";

            pstmt2 = conn.prepareStatement(sql2);

            pstmt2.setInt(1, userId);

            pstmt2.executeUpdate();

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while adding seller.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(null, pstmt1, null);
            closeDB(null, pstmt2, conn);
        }

        return success;
    }

    public void printAllSellersFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT users.user_id, users.username " +
            "FROM users, sellers " +
            "WHERE users.user_id = sellers.seller_id " +
            "ORDER BY users.username";

            rs = stmt.executeQuery(sql);

            System.out.println("Sellers");
            System.out.println("=======");

            while (rs.next()) {
                System.out.println("Seller ID: " + rs.getInt("user_id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println();
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing sellers.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }
    }

    public void printAllBuyersFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT users.user_id, users.username, addresses.state_name, addresses.city_name, " +
            "addresses.street_name, addresses.building_num " +
            "FROM users, buyers, addresses " +
            "WHERE users.user_id = buyers.buyer_id " +
            "AND buyers.address_id = addresses.address_id " +
            "ORDER BY users.username";

            rs = stmt.executeQuery(sql);

            System.out.println("Buyers");
            System.out.println("======");

            while (rs.next()) {
                System.out.println("Buyer ID: " + rs.getInt("user_id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Address: " +
                rs.getString("state_name") + ", " +
                rs.getString("city_name") + ", " +
                rs.getString("street_name") + " " +
                rs.getInt("building_num"));
                System.out.println();
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing buyers.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }
    }

    public boolean isSellerDB(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean exists = false;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT users.user_id " +
                        "FROM users, sellers " +
                        "WHERE users.user_id = sellers.seller_id " +
                        "AND users.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking seller.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return exists;
    }

    public boolean isBuyerDB(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean exists = false;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT users.user_id " +
                        "FROM users, buyers " +
                        "WHERE users.user_id = buyers.buyer_id " +
                        "AND users.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking buyer.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return exists;
    }

    public int getSellerIdByUsernameDB(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int sellerId = -1;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT users.user_id " +
            "FROM users, sellers " +
            "WHERE users.user_id = sellers.seller_id " +
            "AND users.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                sellerId = rs.getInt("user_id");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting seller id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return sellerId;
    }

    public int getBuyerIdByUsernameDB(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int buyerId = -1;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT users.user_id " +
            "FROM users, buyers " +
            "WHERE users.user_id = buyers.buyer_id " +
            "AND users.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                buyerId = rs.getInt("user_id");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting buyer id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return buyerId;
    }

    public boolean isProductForSaleDB(String sellerUsername, String productName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean exists = false;

        try {
            conn = DatabaseConnection.getConnection();

            int sellerId = getSellerIdByUsernameDB(sellerUsername);

            String sql = "SELECT * " +
            "FROM products " +
            "WHERE seller_id = ? " +
            "AND product_name = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, sellerId);
            pstmt.setString(2, productName);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                exists = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking product.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return exists;
    }

    public boolean addProductToDB(String sellerUsername, String productName, double price, Product.Category category, double specialBoxPrice) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();

            int productId = getNextProductId();
            int sellerId = getSellerIdByUsernameDB(sellerUsername);

            String sql = "INSERT INTO products(product_id, seller_id, product_name, price, category, special_box_price) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, productId);
            pstmt.setInt(2, sellerId);
            pstmt.setString(3, productName);
            pstmt.setDouble(4, price);
            pstmt.setString(5, category.toString());
            pstmt.setDouble(6, specialBoxPrice);

            pstmt.executeUpdate();

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while adding product.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(null, pstmt, conn);
        }

        return success;
    }

    public void printProductsByCategoryDB(Product.Category category) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean foundProducts = false;
        int count = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT products.product_name, products.price, products.special_box_price, users.username " +
            "FROM products, sellers, users " +
            "WHERE products.seller_id = sellers.seller_id " +
            "AND sellers.seller_id = users.user_id " +
            "AND products.category = '" + category + "' " +
            "ORDER BY products.product_name";

            rs = stmt.executeQuery(sql);

            System.out.println(category);
            System.out.println("===========");

            while (rs.next()) {
                foundProducts = true;

                System.out.print("Product " + count + ": ");
                System.out.print(rs.getString("product_name"));
                System.out.print(" - " + rs.getDouble("price") + "$");

                if (rs.getDouble("special_box_price") != 0) {
                    System.out.print(" + " + rs.getDouble("special_box_price") + "$ (Special packaging)");
                    System.out.print(" | Total Price: " + (rs.getDouble("price") + rs.getDouble("special_box_price")) + "$");
                }

                System.out.println(" | Seller: " + rs.getString("username") + " |");

                count++;
            }

            if (!foundProducts) {
                System.out.println("No products found in category " + category + ".");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing products by category.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }
    }

    public void printProductsForSellerDB(String sellerUsername) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean foundProducts = false;
        int count = 1;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT products.product_name, products.price, products.special_box_price " +
            "FROM products, sellers, users " +
            "WHERE products.seller_id = sellers.seller_id " +
            "AND sellers.seller_id = users.user_id " +
            "AND users.username = ? " +
            "ORDER BY products.product_name";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sellerUsername);

            rs = pstmt.executeQuery();

            System.out.println("\nSeller: " + sellerUsername);
            System.out.println("List of products");
            System.out.println("----------------");

            while (rs.next()) {
                foundProducts = true;

                System.out.print("Product " + count + ": ");
                System.out.print(rs.getString("product_name"));
                System.out.print(" - " + rs.getDouble("price") + "$");

                if (rs.getDouble("special_box_price") != 0) {
                    System.out.print(" + " + rs.getDouble("special_box_price") + "$ (Special packaging)");
                    System.out.print(" | Total price: " + (rs.getDouble("price") + rs.getDouble("special_box_price")) + "$ |");
                }

                System.out.println();
                count++;
            }

            if (!foundProducts) {
                System.out.println("Currently, this seller has no products for sale.");
            }

            System.out.println("----------------\n");
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing seller products.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }
    }

    public int getProductIdBySellerAndNameDB(String sellerUsername, String productName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int productId = -1;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT products.product_id " +
            "FROM products, sellers, users " +
            "WHERE products.seller_id = sellers.seller_id " +
            "AND sellers.seller_id = users.user_id " +
            "AND users.username = ? " +
            "AND products.product_name = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, sellerUsername);
            pstmt.setString(2, productName);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                productId = rs.getInt("product_id");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while getting product id.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return productId;
    }

    public boolean isProductInCartDB(int buyerId, int productId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean exists = false;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT * " +
            "FROM shopping_cart_items " +
            "WHERE buyer_id = " + buyerId + " " +
            "AND product_id = " + productId;

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                exists = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking shopping cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return exists;
    }

    public boolean addProductToCartDB(String buyerUsername, String sellerUsername, String productName) {
        Connection conn = null;
        Statement stmt = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);
            int productId = getProductIdBySellerAndNameDB(sellerUsername, productName);

            if (buyerId == -1 || productId == -1) {
                return false;
            }

            if (isProductInCartDB(buyerId, productId)) {
                String sql = "UPDATE shopping_cart_items " +
                "SET quantity = quantity + 1 " +
                "WHERE buyer_id = " + buyerId + " " +
                "AND product_id = " + productId;

                stmt.executeUpdate(sql);
            }
            else {
                int cartItemId = getNextCartItemId();

                String sql = "INSERT INTO shopping_cart_items(cart_item_id, buyer_id, product_id, quantity) " +
                "VALUES (" + cartItemId + ", " + buyerId + ", " + productId + ", 1)";

                stmt.executeUpdate(sql);
            }

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while adding product to cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(null, stmt, conn);
        }

        return success;
    }

    public boolean sellerHasProductsDB(String sellerUsername) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean hasProducts = false;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT * " +
            "FROM products, sellers, users " +
            "WHERE products.seller_id = sellers.seller_id " +
            "AND sellers.seller_id = users.user_id " +
            "AND users.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sellerUsername);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                hasProducts = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking seller products.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, pstmt, conn);
        }

        return hasProducts;
    }

    public boolean isCartEmptyDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean isEmpty = true;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT * " +
            "FROM shopping_cart_items " +
            "WHERE buyer_id = " + buyerId;

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                isEmpty = false;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking shopping cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return isEmpty;
    }

    public void printShoppingCartDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean foundProducts = false;
        int count = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT products.product_name, products.price, products.special_box_price, shopping_cart_items.quantity " +
            "FROM shopping_cart_items, products " +
            "WHERE shopping_cart_items.product_id = products.product_id " +
            "AND shopping_cart_items.buyer_id = " + buyerId;

            rs = stmt.executeQuery(sql);

            System.out.println("\nShopping cart");
            System.out.println("-------------");

            while (rs.next()) {
                foundProducts = true;

                System.out.print(count + ". ");
                System.out.print(rs.getString("product_name"));
                System.out.print(" - " + rs.getDouble("price") + "$");

                if (rs.getDouble("special_box_price") != 0) {
                    System.out.print(" + " + rs.getDouble("special_box_price") + "$ (Special packaging)");
                }

                System.out.println(" | Quantity: " + rs.getInt("quantity"));

                count++;
            }

            if (!foundProducts) {
                System.out.println("Shopping cart is currently empty.");
            }

            System.out.println("-------------");
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing shopping cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }
    }

    public double calcCartTotalDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        double total = 0;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT products.price, products.special_box_price, shopping_cart_items.quantity " +
            "FROM shopping_cart_items, products " +
            "WHERE shopping_cart_items.product_id = products.product_id " +
            "AND shopping_cart_items.buyer_id = " + buyerId;

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                double price = rs.getDouble("price");
                double specialBoxPrice = rs.getDouble("special_box_price");
                int quantity = rs.getInt("quantity");

                total += (price + specialBoxPrice) * quantity;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while calculating cart total.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return total;
    }

    public boolean checkoutDB(String buyerUsername, double totalPrice) {
        Connection conn = null;
        PreparedStatement insertOrderStmt = null;
        PreparedStatement selectCartStmt = null;
        PreparedStatement insertOrderItemStmt = null;
        PreparedStatement deleteCartStmt = null;
        ResultSet rs = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);
            int orderId = getNextOrderId();

            String insertOrderSql = "INSERT INTO orders(order_id, buyer_id, transaction_date, total_price) " +
            "VALUES (?, ?, CURRENT_DATE, 0)";

            insertOrderStmt = conn.prepareStatement(insertOrderSql);

            insertOrderStmt.setInt(1, orderId);
            insertOrderStmt.setInt(2, buyerId);

            insertOrderStmt.executeUpdate();

            String cartSql = "SELECT products.product_id, products.product_name, products.price, " +
            "products.special_box_price, shopping_cart_items.quantity " +
            "FROM shopping_cart_items, products " +
            "WHERE shopping_cart_items.product_id = products.product_id " +
            "AND shopping_cart_items.buyer_id = ?";

            selectCartStmt = conn.prepareStatement(cartSql);
            selectCartStmt.setInt(1, buyerId);

            rs = selectCartStmt.executeQuery();

            String insertOrderItemSql = "INSERT INTO order_items(order_item_id, order_id, product_id, quantity, " +
            "product_name, price_at_purchase, special_box_price_at_purchase) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            insertOrderItemStmt = conn.prepareStatement(insertOrderItemSql);

            while (rs.next()) {
                int orderItemId = getNextOrderItemId();

                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                double specialBoxPrice = rs.getDouble("special_box_price");
                int quantity = rs.getInt("quantity");

                insertOrderItemStmt.setInt(1, orderItemId);
                insertOrderItemStmt.setInt(2, orderId);
                insertOrderItemStmt.setInt(3, productId);
                insertOrderItemStmt.setInt(4, quantity);
                insertOrderItemStmt.setString(5, productName);
                insertOrderItemStmt.setDouble(6, price);
                insertOrderItemStmt.setDouble(7, specialBoxPrice);

                insertOrderItemStmt.executeUpdate();
            }

            String deleteCartSql = "DELETE FROM shopping_cart_items " +
            "WHERE buyer_id = ?";

            deleteCartStmt = conn.prepareStatement(deleteCartSql);
            deleteCartStmt.setInt(1, buyerId);

            deleteCartStmt.executeUpdate();

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking out.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, selectCartStmt, null);
            closeDB(null, insertOrderStmt, null);
            closeDB(null, insertOrderItemStmt, null);
            closeDB(null, deleteCartStmt, conn);
        }

        return success;
    }

    public void printOrderHistoryDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean foundOrders = false;
        int currentOrderId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT orders.order_id, orders.transaction_date, orders.total_price, " +
            "order_items.product_name, order_items.price_at_purchase, " +
            "order_items.special_box_price_at_purchase, order_items.quantity " +
            "FROM orders, order_items " +
            "WHERE orders.order_id = order_items.order_id " +
            "AND orders.buyer_id = " + buyerId + " " +
            "ORDER BY orders.order_id, order_items.order_item_id";

            rs = stmt.executeQuery(sql);

            System.out.println("Order history");
            System.out.println("=============");

            while (rs.next()) {
                foundOrders = true;

                int orderId = rs.getInt("order_id");

                if (orderId != currentOrderId) {
                    currentOrderId = orderId;

                    System.out.println();
                    System.out.println("Order ID: " + orderId);
                    System.out.println("Transaction Date: " + rs.getDate("transaction_date"));
                    System.out.println("Total Price: " + rs.getDouble("total_price") + "$");
                    System.out.println("Products:");
                }

                System.out.print("- " + rs.getString("product_name"));
                System.out.print(" | Price: " + rs.getDouble("price_at_purchase") + "$");

                if (rs.getDouble("special_box_price_at_purchase") != 0) {
                    System.out.print(" + " + rs.getDouble("special_box_price_at_purchase") + "$ special box");
                }

                System.out.println(" | Quantity: " + rs.getInt("quantity"));
            }

            if (!foundOrders) {
                System.out.println("No orders history.");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while printing order history.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }
    }

    public boolean hasOrderHistoryDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean hasOrders = false;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT * " +
            "FROM orders " +
            "WHERE buyer_id = " + buyerId;

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                hasOrders = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking order history.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return hasOrders;
    }

    public boolean isBuyerOrderDB(String buyerUsername, int orderId) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        boolean exists = false;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT * " +
            "FROM orders " +
            "WHERE buyer_id = " + buyerId + " " +
            "AND order_id = " + orderId;

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                exists = true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL error while checking order.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, stmt, conn);
        }

        return exists;
    }

    public boolean clearShoppingCartDB(String buyerUsername) {
        Connection conn = null;
        Statement stmt = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "DELETE FROM shopping_cart_items " +
            "WHERE buyer_id = " + buyerId;

            stmt.executeUpdate(sql);

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while clearing shopping cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(null, stmt, conn);
        }

        return success;
    }

    public boolean copyOrderToCartDB(String buyerUsername, int orderId) {
        Connection conn = null;
        Statement selectStmt = null;
        Statement updateStmt = null;
        ResultSet rs = null;

        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();

            selectStmt = conn.createStatement();
            updateStmt = conn.createStatement();

            int buyerId = getBuyerIdByUsernameDB(buyerUsername);

            String sql = "SELECT product_id, quantity " +
                        "FROM order_items " +
                        "WHERE order_id = " + orderId;

            rs = selectStmt.executeQuery(sql);

            while (rs.next()) {
                int cartItemId = getNextCartItemId();
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                String insertSql = "INSERT INTO shopping_cart_items(cart_item_id, buyer_id, product_id, quantity) " +
                                "VALUES (" + cartItemId + ", " + buyerId + ", " + productId + ", " + quantity + ")";

                updateStmt.executeUpdate(insertSql);
            }

            success = true;
        }
        catch (SQLException e) {
            System.out.println("SQL error while copying order to cart.");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        }
        finally {
            closeDB(rs, selectStmt, null);
            closeDB(null, updateStmt, conn);
        }

        return success;
    }

    public void closeDB(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
