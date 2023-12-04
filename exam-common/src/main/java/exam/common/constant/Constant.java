package exam.common.constant;

public final class Constant {

    private Constant() {
    }

    /**
     * 用户权限状态
     */
    public static final String USER_PERM = "EXAM_USER_PERM";

    /**
     * 需要登录的资源地址
     */
    public static final String LOGIN_RESOURCE = "EXAM_LOGIN_RESOURCE";


    /**
     * 用户权限
     */
    public static final String USER_ROLE = "EXAM_USER_ROLE";

    public static final int OPTION_MIN = 65;

    public static final int OPTION_MAX = 90;

    /**
     * 用户手机号正则校验
     */
    public static final String REGEXP_USER_PHONE = "^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(17[0-9]{1})|(18[0-9]{1})|(19[0-9]{1}))+\\d{8})$";

    /**
     * 用户邮箱正则校验
     */
    public static final String REGEXP_USER_EMAIL = "^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$";

    /**
     * 文件路径符号
     */
    public static final String FILE_SEPARATOR = "/";

}
