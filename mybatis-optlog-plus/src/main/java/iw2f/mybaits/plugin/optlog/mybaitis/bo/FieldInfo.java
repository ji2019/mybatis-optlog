package iw2f.mybaits.plugin.optlog.mybaitis.bo;

import lombok.Data;

/**
 * 字段信息
 *
 */
@Data
public class FieldInfo {

    /**
     * 字段名
     */
    private String fieldName;
    /**
     * java字段名
     */
    private String jFieldName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 数据类型
     */
    private String dataType;

    private Boolean primaryKey;
}