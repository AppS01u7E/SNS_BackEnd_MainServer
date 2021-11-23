package com.jinwoo.snsbackend_mainserver.domain.schedule.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class ScheduleRequest{
    @JsonProperty("SchoolSchedule")
    public List<SchoolSchedule> schoolSchedule;


    @Getter
    public static class SchoolSchedule{
        public List<Head> head;
        public List<Row> row;

        @Getter
        public static class Head{
            public int list_total_count;
            @JsonProperty("RESULT")
            public RESULT rESULT;
        }

        @Getter
        public static class Row{
            @JsonProperty("ATPT_OFCDC_SC_CODE")
            public String aTPT_OFCDC_SC_CODE;
            @JsonProperty("ATPT_OFCDC_SC_NM")
            public String aTPT_OFCDC_SC_NM;
            @JsonProperty("SD_SCHUL_CODE")
            public String sD_SCHUL_CODE;
            @JsonProperty("SCHUL_NM")
            public String sCHUL_NM;
            @JsonProperty("AY")
            public String aY;
            @JsonProperty("DGHT_CRSE_SC_NM")
            public String dGHT_CRSE_SC_NM;
            @JsonProperty("SCHUL_CRSE_SC_NM")
            public String sCHUL_CRSE_SC_NM;
            @JsonProperty("SBTR_DD_SC_NM")
            public String sBTR_DD_SC_NM;
            @JsonProperty("AA_YMD")
            public String aA_YMD;
            @JsonProperty("EVENT_NM")
            public String eVENT_NM;
            @JsonProperty("EVENT_CNTNT")
            public String eVENT_CNTNT;
            @JsonProperty("ONE_GRADE_EVENT_YN")
            public String oNE_GRADE_EVENT_YN;
            @JsonProperty("TW_GRADE_EVENT_YN")
            public String tW_GRADE_EVENT_YN;
            @JsonProperty("THREE_GRADE_EVENT_YN")
            public String tHREE_GRADE_EVENT_YN;
            @JsonProperty("FR_GRADE_EVENT_YN")
            public String fR_GRADE_EVENT_YN;
            @JsonProperty("FIV_GRADE_EVENT_YN")
            public String fIV_GRADE_EVENT_YN;
            @JsonProperty("SIX_GRADE_EVENT_YN")
            public String sIX_GRADE_EVENT_YN;
            @JsonProperty("LOAD_DTM")
            public String lOAD_DTM;
        }

        @Getter
        public static class RESULT{
            @JsonProperty("CODE")
            public String cODE;
            @JsonProperty("MESSAGE")
            public String mESSAGE;
        }


    }
}


