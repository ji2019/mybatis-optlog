package iw2f.mybaits.plugin.optlog.mybaitis.bo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class EditBo {

    List<Map<String,Object>> primaryKey ;

    List<CompareResult> modifyColumns;

}
