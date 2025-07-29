package com.caicai.handler;

import com.caicai.enums.PostStatusType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis 类型处理器，用于处理 PostStatusType 枚举和数据库整数值之间的转换
 */
@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(PostStatusType.class)
public class EnumStatusTypeHandler extends BaseTypeHandler<PostStatusType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PostStatusType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public PostStatusType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return rs.wasNull() ? null : PostStatusType.getByCode(code);
    }

    @Override
    public PostStatusType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : PostStatusType.getByCode(code);
    }

    @Override
    public PostStatusType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : PostStatusType.getByCode(code);
    }
} 