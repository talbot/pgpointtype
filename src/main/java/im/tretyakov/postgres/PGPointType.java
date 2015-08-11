package im.tretyakov.postgres;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.geometric.PGpoint;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * PostgresQL geometry PGPoint user type implementation
 * <p>
 * Created on 29.07.15.
 *
 * @author tretyakov (dmitry@tretyakov.im)
 */
public class PGPointType extends PGpoint implements UserType {

    /**
     * Return the SQL type codes for the columns mapped by this type. The codes are defined on <tt>java.sql.Types</tt>.
     *
     * @return int[] the type codes
     * @see Types
     */
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     *
     * @return Class
     */
    public Class returnedClass() {
        return PGpoint.class;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence "equality". Equality of the persistent
     * state.
     *
     * @param x First point
     * @param y Second point
     * @return boolean
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        return x instanceof PGpoint && y instanceof PGpoint && x.equals(y);
    }

    /**
     * Get a hashcode for the instance, consistent with persistence "equality"
     *
     * @param x Object on which hashcode will be calculated
     */
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC result set. Implementors should handle possibility of null
     * values.
     *
     * @param rs      a JDBC result set
     * @param names   the column names
     * @param session a JDBC session
     * @param owner   the containing entity  @return Object
     * @throws HibernateException
     * @throws SQLException
     */
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        assert names.length == 1;
        if (rs.wasNull()) {
            return null;
        }
        final Object result = rs.getObject(names[0]);
        return new PGpoint(result.toString());
    }

    /**
     * Write an instance of the mapped class to a prepared statement. Implementors should handle possibility of null
     * values. A multi-column type should be written to parameters starting from <tt>index</tt>.
     *
     * @param st      a JDBC prepared statement
     * @param value   the object to write
     * @param index   statement parameter index
     * @param session a JDBC session
     * @throws HibernateException
     * @throws SQLException
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setObject(index, new PGpoint(0.0, 0.0));
        } else {
            PGpoint v = (PGpoint) value;
            st.setObject(index, v);
        }
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at collections. It is not necessary to copy
     * immutable objects, or null values, in which case it is safe to simply return the argument.
     *
     * @param value the object to be cloned, which may be null
     * @return Object a copy
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     * Are objects of this type mutable?
     *
     * @return boolean
     */
    public boolean isMutable() {
        return true;
    }

    /**
     * Transform the object into its cacheable representation. At the very least this method should perform a deep copy
     * if the type is mutable. That may not be enough for some implementations, however; for example, associations must
     * be cached as identifier values. (optional operation)
     *
     * @param value the object to be cached
     * @return a cachable representation of the object
     * @throws HibernateException
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * Reconstruct an object from the cacheable representation. At the very least this method should perform a deep copy
     * if the type is mutable. (optional operation)
     *
     * @param cached the object to be cached
     * @param owner  the owner of the cached object
     * @return a reconstructed object from the cachable representation
     * @throws HibernateException
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to with a new (original) value
     * from the detached entity we are merging. For immutable objects, or null values, it is safe to simply return the
     * first parameter. For mutable objects, it is safe to return a copy of the first parameter. For objects with
     * component values, it might make sense to recursively replace component values.
     *
     * @param original the value from the detached entity being merged
     * @param target   the value in the managed entity
     * @param owner the owner of the object
     * @return the value to be merged
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        if (original instanceof PGpoint) {
            final PGpoint value = (PGpoint) original;
            return new PGpoint(value.x, value.y);
        } else {
            return new PGpoint(0.0, 0.0);
        }
    }
}
