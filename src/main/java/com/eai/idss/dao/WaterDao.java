package com.eai.idss.dao;


import java.util.List;
import java.util.Map;


import com.eai.idss.model.WaterMasterData;
import com.eai.idss.model.WaterStationMaster;
import com.eai.idss.vo.*;
import org.bson.conversions.Bson;
import org.springframework.data.domain.Pageable;
public interface WaterDao {



    public List<WaterStationListResponseVo> getWaterStationList(WaterStationRegionRequest wsr);
    public  List<WaterDataResponseVo> getWaterStationDetailStationId(WaterDetailRequest wdr);

}
