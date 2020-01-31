package com.spring.batch.ifx.queryprovider;

import java.lang.reflect.Field;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.AbstractSqlPagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryUtils;
import org.springframework.util.StringUtils;

public class IfxSqlPagingQueryProvider extends AbstractSqlPagingQueryProvider {

	private boolean usingParameters;

	@Override
	public void init(DataSource dataSource) throws Exception {
		super.init(dataSource);
		Field usingNamedParameters = this.getClass().getSuperclass().getDeclaredField("usingNamedParameters");
		usingNamedParameters.setAccessible(true);
		if (usingNamedParameters.get(this) != null && (Boolean) usingNamedParameters.get(this)) {
			usingParameters = true;
		}

		if (!this.getSortKeys().isEmpty()) {
			usingParameters = true;
		}
	}

	@Override
	public boolean isUsingNamedParameters() {
		return usingParameters;
	}

	@Override
	public String getSortKeyPlaceHolder(String keyName) {
		return this.isUsingNamedParameters() ? ":_" + keyName : "?";
	}

	@Override
	public String generateFirstPageQuery(int pageSize) {
		return generateLimitSqlQuery(false, pageSize);
	}

	@Override
	public String generateRemainingPagesQuery(int pageSize) {
		return generateLimitSqlQuery(true, pageSize);
	}

	@Override
	public String generateJumpToItemQuery(int itemIndex, int pageSize) {
		int page = itemIndex / pageSize;
		int offset = (page * pageSize);
		offset = offset < 0 ? 0 : offset;
		return generateLimitJumpToQuery(offset);
	}

	public String generateLimitSqlQuery(boolean remainingPageQuery, int pageSize) {
		StringBuilder sql = new StringBuilder();
		if (remainingPageQuery) {
			sql.append("SELECT SKIP ").append(pageSize).append(" LIMIT ").append(pageSize).append(" ")
					.append(this.getSelectClause());
		} else {
			sql.append("SELECT FIRST ").append(pageSize).append(" ").append(this.getSelectClause());
		}
		sql.append(" FROM ").append(this.getFromClause());
		buildWhereClause(sql);
		buildGroupByClause(sql);
		if (remainingPageQuery) {
			sql.append(" AND ");
			SqlPagingQueryUtils.buildSortConditions(this, sql);
		}
		return sql.toString();
	}

	public String generateLimitJumpToQuery(int offset) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SKIP ").append(offset - 1).append(" LIMIT 1 ").append(buildSortKeySelect());
		sql.append(" FROM ").append(this.getFromClause());
		buildWhereClause(sql);
		buildGroupByClause(sql);
		return sql.toString();
	}

	private String buildSortKeySelect() {
		StringBuilder select = new StringBuilder();
		String prefix = "";
		if (this.getSortKeys().isEmpty()) {
			return "";
		}
		for (Map.Entry<String, Order> sortKey : this.getSortKeys().entrySet()) {
			select.append(prefix);
			prefix = ", ";
			select.append(sortKey.getKey());
		}
		return select.toString();
	}

	private void buildWhereClause(StringBuilder sql) {
		if (StringUtils.hasText(this.getWhereClause())) {
			sql.append(" WHERE ");
			sql.append(this.getWhereClause());
		}
	}

	private void buildGroupByClause(StringBuilder sql) {
		if (StringUtils.hasText(this.getGroupClause())) {
			sql.append(" GROUP BY ");
			sql.append(this.getGroupClause());
		}
	}
}