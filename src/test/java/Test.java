import com.ruubypay.log.annotation.FieldIgnore;
import com.ruubypay.log.annotation.Sensitive;
import com.ruubypay.log.util.SensitiveFieldUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * @Author liu_penghui
 * @Date 2018/10/19.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.getCtClass("com.ruubypay.log.Base");
        CtMethod m = ctClass.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = ctClass.toClass();
    }
}

class ModelsReturnT<T> {
    @Sensitive
    private T resData;                //返回数据
    private String resCode;                //错误代码
    private String resMessage;             //错误信息

    public void printObjectValue(Object object, String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object ot = Class.forName(className).newInstance();
        Class c = ot.getClass();
        // 取得所有类成员变量
        Field[] fields = FieldUtils.getAllFields(c);
        // 取消每个属性的安全检查
        for(Field field : fields){
            field.setAccessible(true);
            try {
                if (null !=field.getAnnotation(FieldIgnore.class)) {
                    Field[] fields1 = FieldUtils.getAllFields(field.get(object).getClass());
                    for (Field field1 : fields1) {
                        field1.setAccessible(true);
                        try {
                            String name = field1.getName();
                            Object argument = field1.get(field.get(object));
                            // 如果FieldOperate注解ignore值为true则不打印该字段内容
                            if (null != field.getAnnotation(FieldIgnore.class)) {
                                continue;
                            }
                            argument = SensitiveFieldUtil.getSensitiveArgument(field1, argument);
                            //result.put(name, argument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public T getResData() {
        return resData;
    }

    public void setResData(T resData) {
        this.resData = resData;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMessage() {
        return resMessage;
    }

    public void setResMessage(String resMessage) {
        this.resMessage = resMessage;
    }

    @Override
    public String toString() {
        return "ModelsReturnT{" +
                "resData=" + resData +
                ", resCode='" + resCode + '\'' +
                ", resMessage='" + resMessage + '\'' +
                '}';
    }
}

class UserInfo {
    @FieldIgnore
    private String idcard;
    private String realname;
    private String bankcard;
    private String mobile;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
