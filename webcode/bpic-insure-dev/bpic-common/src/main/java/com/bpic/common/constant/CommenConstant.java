package com.bpic.common.constant;

public class CommenConstant {


    public static final Integer ER_PAPER_STATUS = 1;  // 定义常量

    public static final String IMGPATH = "IMGPATH";//图片上传路径
    public static final String SUFFIX = "png";//图片后缀


    /*
     * 身份证件类型
     *
     * */
    public static final String CARDSFZ = "111";//身份证
    public static final String CARDHZ = "414";//护照
    public static final String CARDTBZ = "511";//台胞证
    public static final String CARDGA = "516";//港澳回乡证

    /*
     * 附件类型   ER
     * */
    public static final Integer ER_IDEN_PHOTO_POSITIVE = 1;//证件照正面
    public static final Integer ER_IDEN_PHOTO_REVERSE = 2;//证件照反面
    public static final Integer ER_HEAD_PHOTO = 3;//头像
    public static final Integer ER_EDUCATION_PROVE = 4;//学历证明
    public static final Integer ER_XUEXIN_PROVE = 5;//学信网验证页面
    public static final Integer ER_RECOMMEND_RELATION_SIGN = 6;//引荐关系签名
    public static final Integer ER_NEWCOMER_SIGN = 7;//准新人签名（准增员声明授权文件）
    public static final Integer ER_TESTPAPER_ICON = 8;//试卷icon
    public static final Integer ER_CHUSHEN_CONFIRM_PAPER = 9;//初审面试人才计划更改确认书
    public static final Integer ER_ZHENXUAN_CONFIRM_PAPER = 10;//甄选面试人才计划更改确认书
    public static final Integer ER_JUEDING_CONFIRM_PAPER = 11;//决定面试人才计划更改确认书
    public static final Integer ER_LEAVEJOB_PROVE = 12;//离职证明（未满6月）
    public static final Integer ER_FOREIGNPERSON_JOB_PROVE = 13;//外籍人员就业证明
    public static final Integer ER_PASSPORT = 14;//护照
    public static final Integer ER_BIRTH_CERTIFICATE = 15;//出生证
    public static final Integer ER_MILITCARY_CARD = 16;//军人证
    public static final Integer ER_MTP = 17;//台胞证
    public static final Integer ER_GERMAN_RETURN_CERTIFICATE_POSITIVE = 18;//港澳回乡证  正面
    public static final Integer ER_GERMAN_RETURN_CERTIFICATE_REVERSE = 19;//港澳回乡证  反面
    public static final Integer ER_FIRST_TRIAL_INTERVIEW_SIGNATURE = 20;//初选面试签名
    public static final Integer ER_SELECTION_INTERVIEW_SIGNATURE = 21;//甄选面试签名
    public static final Integer ER_DECIDE_INTERVIEW_SIGNATURE = 22;//决定面试签名
    public static final Integer ER_ZENGYUAN_SIGNATURE = 23;//增员签名
    public static final Integer ER_PAGE_ICON = 26;//试卷图标
    public static final Integer ER_Free_Crown_ = 27;//免冠照片

    /*
     * 附件类型   EL
     * */
    public static final Integer EL_THUMBNAIL = 24;//缩略图
    public static final Integer EL_LESSON_COURSEWARE = 30;//课程课件
    public static final Integer EL_LESSON_ATTACHMENT = 31;//课程附件
    public static final Integer EL_MATERIAL_ATTACHMENT = 32;//资料附件
    public static final Integer EL_ACTIVITY_BANNER = 33;//活动banner
    public static final Integer EL_REWARD_CREDENTIALS = 34;//奖励证书
    public static final Integer EL_CONTRACT_SIGN_ATTACHMENT = 35;//签约签名附件
    public static final Integer EL_PERSON_RESTRICT = 40;//人员限制附件
    public static final Integer ER_BANK = 42;//银行卡
    public static final Integer EL_Honor_Qualification = 43;//MDRT荣誉资格


    //1报名，2初选面试，3甄选面试，4决定面试，5培训，6核查，7签约
    public static final String ER_Flow_NO_PASS = "-1";//不通过
    public static final String ER_Flow_signup = "1";//模块名报名
    public static final String ER_Flow_first_time = "2";//模块名 初选面试
    public static final String ER_Flow_Selection = "3";//模块名甄选面试
    public static final String ER_Flow_Decision = "4";//模块名决定面试
    public static final String ER_Flow_ONLINE_REGISTRATION = "5";//模块名培训报名
    public static final String ER_Flow_TRAINING_TEST = "6";//模块名培训考试
    public static final String ER_Flow_SIGNED = "7";//模块名签约
    public static final String ER_Flow_COMPLETE = "8";//完成签约




}
