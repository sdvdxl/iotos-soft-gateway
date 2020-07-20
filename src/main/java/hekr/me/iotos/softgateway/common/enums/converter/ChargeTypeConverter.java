package hekr.me.iotos.softgateway.common.enums.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import hekr.me.iotos.softgateway.common.enums.ChargeSource;
import hekr.me.iotos.softgateway.common.enums.ChargeType;

/**
 * @author jiatao
 * @date 2020/7/20
 */
public class ChargeTypeConverter implements Converter<Integer, ChargeType> {

  @Override
  public ChargeType convert(Integer value) {
    return ChargeType.valueOf(value);
  }

  @Override
  public JavaType getInputType(TypeFactory typeFactory) {
    return typeFactory.constructType(Integer.class);
  }

  @Override
  public JavaType getOutputType(TypeFactory typeFactory) {
    return typeFactory.constructType(ChargeType.class);
  }
}
