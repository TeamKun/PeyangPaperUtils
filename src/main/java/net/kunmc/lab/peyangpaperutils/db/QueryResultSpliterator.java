package net.kunmc.lab.peyangpaperutils.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;

class QueryResultSpliterator implements Spliterator<ResultRow>
{
    private final ResultSet result;
    private final boolean closeConnectionOnException;

    public QueryResultSpliterator(ResultSet result, boolean closeConnectionOnException)
    {
        this.result = result;
        this.closeConnectionOnException = closeConnectionOnException;
    }

    @Override
    public boolean tryAdvance(Consumer<? super ResultRow> action)
    {
        try
        {
            if (this.result.next())
            {
                action.accept(new ResultRow(this.result, this.closeConnectionOnException));
                return true;
            }
            else
                return false;
        }
        catch (SQLException e)
        {
            if (this.closeConnectionOnException)
            {
                try
                {
                    this.result.getStatement().getConnection().close();
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Spliterator<ResultRow> trySplit()
    {
        return null;
    }

    @Override
    public long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics()
    {
        return ORDERED;
    }
}
