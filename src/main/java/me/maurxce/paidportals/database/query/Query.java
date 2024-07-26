package me.maurxce.paidportals.database.query;

public class Query {
    // ========== ECONOMY ==========
    public static String CREATE_TABLE_ECONOMY = """
    CREATE TABLE IF NOT EXISTS pp_economy (
        integrity ENUM("") NOT NULL,
        paid DECIMAL(8, 2) NOT NULL,
        PRIMARY KEY (integrity)
    );
    """;

    public static String GET_POOL_BALANCE = """
    SELECT paid
    FROM pp_economy;
    """;

    public static String SET_POOL_BALANCE = """
    INSERT INTO pp_economy (paid)
    VALUES (?)
    ON DUPLICATE KEY UPDATE paid = ?;
    """;

    // ========== DIMENSIONS ==========
    public static String CREATE_TABLE_DIMENSIONS = """
    CREATE TABLE IF NOT EXISTS pp_dimensions (
        id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
        name VARCHAR(32) NOT NULL,
        is_locked BOOLEAN NOT NULL,
        PRIMARY KEY (id)
    );
    """;

    public static String GET_DIMENSION_STATUS = """
    SELECT name, is_locked
    FROM pp_dimensions;
    """;

    public static String SET_DIMENSION_STATUS = """
    INSERT INTO pp_dimensions (name, is_locked)
    VALUES (?, ?)
    ON DUPLICATE KEY UPDATE is_locked = ?;
    """;
}
