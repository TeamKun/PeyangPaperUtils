package net.kunmc.lab.peyangpaperutils.db;

import lombok.Getter;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBのトランザクションを簡単に行うためのクラスです。
 */
public class Transaction implements AutoCloseable
{
    /**
     * Dbのコネクションです。
     */
    @Getter
    private final Connection connection;

    @Nullable
    private final PreparedStatement preparedStatement;

    private TransactionRun beforeCommit;

    private Transaction(Connection connection, String query) throws SQLException
    {
        this.connection = connection;
        this.connection.setAutoCommit(false);

        if (query != null)
            this.preparedStatement = this.connection.prepareStatement(query);
        else
            this.preparedStatement = null;
    }

    /**
     * トランザクションを開始します。
     *
     * @param connection コネクション
     * @param sql        SQL文
     * @return トランザクション
     */
    public static Transaction create(@NotNull Connection connection, @Nullable @Language("sql") String sql)
    {
        try
        {
            return new Transaction(connection, sql);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * トランザクションを開始します。
     *
     * @param transaction 既存のトランザクション
     * @param sql         SQL文
     * @return トランザクション
     */
    public static Transaction create(@NotNull Transaction transaction, @Nullable @Language("sql") String sql)
    {
        try
        {
            return new Transaction(transaction.getConnection(), sql);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * トランザクションを再生成します。
     *
     * @param newQuery 新しいSQL文
     * @return トランザクション
     */
    public Transaction renew(@Language("sql") @Nullable String newQuery)
    {
        return create(this, newQuery);
    }

    /**
     * コミット前に実行する処理を登録します。
     *
     * @param beforeCommit コミット前に実行する処理
     * @return トランザクション
     */
    public Transaction beforeCommit(TransactionRun beforeCommit)
    {
        this.beforeCommit = beforeCommit;
        return this;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkPrepareCondition()
    {
        return this.preparedStatement != null;
    }

    /**
     * SQL文に文字列値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, @Nullable String value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setString(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }

    }

    /**
     * SQL文に32ビット整数値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, int value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setInt(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文に真偽値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, boolean value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setBoolean(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文に64ビット整数値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, long value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setLong(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文に64ビット浮動小数点数値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, double value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setDouble(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文に32ビット浮動小数点数値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, float value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setFloat(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文にバイナリ値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, byte value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setByte(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文に16ビット整数値をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, short value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setShort(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文にバイト配列をセットします。
     *
     * @param index インデックス
     * @param value 値
     * @return トランザクション
     */
    public Transaction set(int index, byte[] value)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setBytes(index, value);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SQL文にNULL値をセットします。
     *
     * @param index インデックス
     * @param type  型
     * @return トランザクション
     */
    public Transaction setNull(int index, int type)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            this.preparedStatement.setNull(index, type);
            return this;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 更新系SQL文を実行します。
     *
     * @param autoFinish 自動終了するかどうか
     * @return 更新件数
     */
    public int executeUpdate(boolean autoFinish)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            int result = this.preparedStatement.executeUpdate();

            if (autoFinish)
            {
                if (this.beforeCommit != null)
                    this.beforeCommit.run(this);

                this.connection.commit();
            }

            return result;
        }
        catch (SQLException e)
        {
            try
            {
                this.connection.rollback();
            }
            catch (SQLException e1)
            {
                throw new IllegalStateException(e1);
            }

            throw new IllegalStateException(e);
        }
        finally
        {

            if (autoFinish)
                try
                {
                    this.connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 更新系SQL文を実行します。
     *
     * @return 更新件数
     */
    public int executeUpdate()
    {
        return this.executeUpdate(true);
    }

    /**
     * クエリ系SQL文を実行します。
     *
     * @param <T> 戻り値の型
     */
    public <T> QueryResult<T> executeQuery()
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            ResultSet resultSet = this.preparedStatement.executeQuery();

            return new QueryResult<>(resultSet);
        }
        catch (SQLException e)
        {
            try
            {
                this.connection.rollback();
                this.connection.close();
            }
            catch (SQLException e1)
            {
                throw new IllegalStateException(e1);
            }

            throw new IllegalStateException(e);
        }
    }

    /**
     * トランザクションを実行し、コミットします。
     *
     * @param transactionRun トランザクションを処理する関数
     */
    public void doTransaction(TransactionRun transactionRun)
    {
        try
        {
            transactionRun.run(this);

            if (this.beforeCommit != null)
                this.beforeCommit.run(this);
            this.connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                this.connection.rollback();
            }
            catch (SQLException e1)
            {
                throw new IllegalStateException(e1);
            }

            throw new IllegalStateException(e);
        }
        finally
        {
            try
            {
                this.connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 手動でトランザクションをコミットし終了します。
     */
    public void finishManually()
    {
        try
        {
            if (this.beforeCommit != null)
                this.beforeCommit.run(this);
            this.connection.commit();
        }
        catch (SQLException e)
        {
            try
            {
                this.connection.rollback();
            }
            catch (SQLException e1)
            {
                throw new IllegalStateException(e1);
            }

            throw new IllegalStateException(e);
        }
        finally
        {
            try
            {
                this.connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 手動でトランザクションをロールバックし終了します。
     */
    public void abortManually()
    {
        try
        {
            this.connection.rollback();
        }
        catch (SQLException e1)
        {
            throw new IllegalStateException(e1);
        }

        try
        {
            this.connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * DBにレコードが存在するかどうかを確認します。
     *
     * @param closeConnection 終了時にコネクションを閉じるかどうか
     * @return 存在するかどうか
     */
    public boolean isExists(boolean closeConnection)
    {
        if (!this.checkPrepareCondition())
            throw new IllegalStateException("This TransactionHelper is not prepared.");

        try
        {
            ResultSet resultSet = this.preparedStatement.executeQuery();

            boolean result = resultSet.next();

            resultSet.close();

            return result;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            try
            {
                if (closeConnection)
                    this.connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * DBにレコードが存在するかどうかを確認します。
     *
     * @return 存在するかどうか
     */
    public boolean isExists()
    {
        return this.isExists(true);
    }

    @Override
    public void close()
    {
        try
        {
            if (!this.connection.isClosed())
                this.connection.close();
        }
        catch (SQLException ignored)
        {
        }
    }


}
