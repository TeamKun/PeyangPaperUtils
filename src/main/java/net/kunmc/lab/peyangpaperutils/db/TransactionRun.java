package net.kunmc.lab.peyangpaperutils.db;

import java.sql.SQLException;

/**
 * トランザクションを実行する汎用関数です。
 */
@FunctionalInterface
public interface TransactionRun
{
    void run(Transaction transaction) throws SQLException;
}
