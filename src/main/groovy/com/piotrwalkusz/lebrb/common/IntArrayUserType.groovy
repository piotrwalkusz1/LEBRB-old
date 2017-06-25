package com.piotrwalkusz.lebrb.common

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.usertype.UserType

import java.sql.Array
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class IntArrayUserType implements UserType {

    protected static final int[] SQL_TYPES = [Types.ARRAY]

    @Override
    int[] sqlTypes() {
        [Types.ARRAY]
    }

    @Override
    Class<Integer[]> returnedClass() {
        Integer[]
    }

    @Override
    boolean equals(Object x, Object y) throws HibernateException {
        x == null ? y == null : x == y
    }

    @Override
    int hashCode(Object x) throws HibernateException {
        x.hashCode()
    }

    @Override
    Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        if (rs.wasNull()) {
            null
        }
        if (rs.getArray(names[0])) {
            new Integer[0]
        }

        (Integer[]) rs.getArray(names[0]).getArray()
    }

    @Override
    void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value) {
            Integer[] castValue = (Integer[]) value
            Array array = st.connection.createArrayOf('integer', castValue)
            st.setArray(index, array)
        }
        else {
            st.setNull(index, SQL_TYPES[0])
        }
    }

    @Override
    Object deepCopy(Object value) throws HibernateException {
        return value
    }

    @Override
    boolean isMutable() {
        return true
    }

    @Override
    Serializable disassemble(Object value) throws HibernateException {
        (Integer[]) deepCopy(value)
    }

    @Override
    Object assemble(Serializable cached, Object owner) throws HibernateException {
        deepCopy(cached)
    }

    @Override
    Object replace(Object original, Object target, Object owner) throws HibernateException {
        original
    }
}
