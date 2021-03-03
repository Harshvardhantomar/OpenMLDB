/*
 * java/fesql-common/src/main/java/com/_4paradigm/fesql/sqlcase/model/SQLCase.java
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.fesql.sqlcase.model;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SQLCase implements Serializable{
    int level = 0;
    String id;
    String desc;
    String mode;
    String db;
    String sql;
    List<List<String>> dataProvider;
    List<String> sqls;
    boolean standard_sql;
    boolean standard_sql_compatible;
    List<String> tags;
    List<String> common_column_indices;
    String batch_plan;
    String request_plan;
    String cluster_request_plan;
    List<InputDesc> inputs;
    InputDesc batch_request;
    ExpectDesc expect;
    String spName = genAutoName();
    UnequalExpect unequalExpect;

    private Map<Integer,ExpectDesc> expectProvider;

    public static String formatSql(String sql, int idx, String name) {
        return sql.replaceAll("\\{" + idx + "\\}", name);
    }

    public int getLevel() {
        return this.level;
    }

    public static String formatSql(String sql, String name) {
        return sql.replaceAll("\\{auto\\}", name);
    }

    public String getSql() {
        sql = formatSql(sql, Table.genAutoName());
        if (CollectionUtils.isEmpty(inputs)) {
            return sql;
        }
        for (int idx = 0; idx < inputs.size(); idx++) {
            sql = formatSql(sql, idx, inputs.get(idx).getName());
        }
        return sql;
    }


    public static String genAutoName() {
        return "auto_" + RandomStringUtils.randomAlphabetic(8);
    }

    public String getProcedure(String sql) {
        return buildCreateSpSQLFromColumnsIndexs(spName, sql, inputs.get(0).getColumns());
    }

    public static String buildCreateSpSQLFromColumnsIndexs(String name, String sql, List<String> columns) {
        if (sql == null || sql.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("create procedure " + name + "(\n");
        for (int i = 0; i < columns.size(); i++) {
            builder.append(columns.get(i));
            if (i != columns.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(")\n");
        builder.append("BEGIN\n");
        builder.append(sql);
        builder.append("\n");
        builder.append("END;");
        sql = builder.toString();
        return sql;
    }

    public ExpectDesc getOnlineExpectByType(SQLCaseType sqlCaseType){
        ExpectDesc expect = this.getExpect();
        UnequalExpect unequalExpect = this.getUnequalExpect();
        if(unequalExpect==null) return expect;
        switch (sqlCaseType){
            case kDDL:
            case kBatch:
                ExpectDesc batch_expect = unequalExpect.getBatch_expect();
                if(batch_expect!=null) return batch_expect;
                break;
            case kRequestWithSp:
            case kRequestWithSpAsync:
                ExpectDesc sp_expect = unequalExpect.getSp_expect();
                if(sp_expect!=null) return sp_expect;
            case kRequest:
                ExpectDesc request_expect = unequalExpect.getRequest_expect();
                if(request_expect!=null) return request_expect;
                break;
            case kBatchRequest:
            case kBatchRequestWithSp:
            case kBatchRequestWithSpAsync:
                ExpectDesc request_batch_expect = unequalExpect.getRequest_batch_expect();
                if(request_batch_expect!=null) return request_batch_expect;
                break;
        }
        ExpectDesc onlineExpect = unequalExpect.getOnline_expect();
        if(onlineExpect!=null) return onlineExpect;
        ExpectDesc euqalsExpect = unequalExpect.getExpect();
        if(euqalsExpect!=null) return euqalsExpect;
        return expect;
    }
    public ExpectDesc getOfflineExpectByType(){
        ExpectDesc expect = this.getExpect();
        UnequalExpect unequalExpect = this.getUnequalExpect();
        if(unequalExpect==null) return expect;
        ExpectDesc offlineExpect = unequalExpect.getOffline_expect();
        if(offlineExpect!=null) return offlineExpect;
        ExpectDesc euqalsExpect = unequalExpect.getExpect();
        if(euqalsExpect!=null) return euqalsExpect;
        return expect;
    }
}
