package hekr.me.iotos.softgateway.common.enums.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import hekr.me.iotos.softgateway.common.enums.ChargeSource;

/**
 * @author jiatao
 * @date 2020/7/20
 */
public class ChargeSourceConverter implements Converter<Integer, ChargeSource> {

  @Override
  public ChargeSource convert(Integer value) {
    return ChargeSource.valueOf(value);
  }

  @Override
  public JavaType getInputType(TypeFactory typeFactory) {
    return typeFactory.constructType(Integer.class);
  }

  @Override
  public JavaType getOutputType(TypeFactory typeFactory) {
    return typeFactory.constructType(ChargeSource.class);
  }
}
