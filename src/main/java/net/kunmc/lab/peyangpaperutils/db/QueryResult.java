package net.kunmc.lab.peyangpaperutils.db;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * クエリの実行結果を表すクラスです。
 */
@RequiredArgsConstructor
public class QueryResult<T>
{
    private final ResultSet result;
    @Setter
    @Accessors(chain = true)
    private Function<ResultRow, T> mapper;

    /**
     * ResultSetをそのまま取得します。
     *
     * @return ResultSet
     */
    public ResultSet getResult()
    {
        return this.result;
    }

    /**
     * この結果を解放します。
     */
    public void close() throws SQLException
    {
        this.result.close();
    }

    /**
     * Listに変換します。
     *
     * @param resultMapper マッピング関数
     * @param max          最大件数
     * @return 変換されたList
     */
    public ArrayList<T> mapToList(Function<? super ResultRow, ? extends T> resultMapper, long max)
    {
        ArrayList<T> list = new ArrayList<>();

        try
        {
            while (this.result.next() && !(max == -1 || list.size() >= max))
                list.add(resultMapper.apply(new ResultRow(this.result, true)));
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }

        return list;
    }

    /**
     * Listに変換します。
     *
     * @param resultMapper マッピング関数
     * @return 変換されたList
     */
    public ArrayList<T> mapToList(Function<ResultRow, T> resultMapper)
    {
        return this.mapToList(resultMapper, -1);
    }

    /**
     * Streamに変換します。
     *
     * @return 変換されたStream
     */
    public Stream<ResultRow> stream(boolean closeConnectionOnException)
    {
        return StreamSupport.stream(
                new QueryResultSpliterator(this.result, closeConnectionOnException),
                false
        );
    }

    /**
     * Streamに変換します。
     *
     * @return 変換されたStream
     */
    public Stream<ResultRow> stream()
    {
        return this.stream(true);
    }

    /**
     * 次の行に移動します。
     *
     * @return 次の行が存在するかどうか
     */
    public boolean next()
    {
        try
        {
            return this.result.next();
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 行の内容をマップして返します。
     *
     * @return マップされた行の内容
     */
    public T get()
    {
        if (this.mapper == null)
            throw new IllegalStateException("Mapper is not set.");

        return this.mapper.apply(new ResultRow(this.result, true));
    }
}
