package hk.opensource.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dozer.DozerBeanMapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanListUtilsWithDozer {

  private DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

  public DozerBeanMapper getBeanMapper() {
    return dozerBeanMapper;
  }

  public void setMappingFile(List<String> mappingFileUrls) {
    dozerBeanMapper.setMappingFiles(mappingFileUrls);
  }

  public <S, T> T convertFirstObjInList(List<?> src, Class<T> targetClass) {
    return convertFirstObjInList(src, s -> dozerBeanMapper.map(s, targetClass));
  }

  public <S, T> List<T> convertListToList(List<?> src, Class<T> targetClass) {
    return convertListToList(src, s -> dozerBeanMapper.map(s, targetClass));
  }

  public <S, T> T convertFirstObjInList(List<S> src, Function<S, T> converter) {
    return convertFirstObjInList(src, converter, null);
  }

  public <S, T> T convertFirstObjInList(List<S> src, Function<S, T> converter,
      Supplier<T> initialObj) {
    T result = (initialObj == null) ? null : initialObj.get();
    if (!src.isEmpty()) {
      result = converter.apply(src.get(0));
    }
    return result;
  }

  public <S, T> List<T> convertListToList(List<S> src, Function<S, T> converter) {
    List<T> result = Collections.emptyList();
    if (!src.isEmpty()) {
      result = new ArrayList<>(src.size());
      for (S s : src) {
        result.add(converter.apply(s));
      }
    }
    return result;
  }

  public <S, K, V> Map<K, V> convertListToMap(List<S> src, Function<S, Entry<K, V>> converter) {
    Map<K, V> result = Collections.emptyMap();
    if (!src.isEmpty()) {
      result = new HashMap<>();
      for (S s : src) {
        Entry<K, V> entry = converter.apply(s);
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  public <T> Map<String, Object> convertObjectToMap(T object) throws IntrospectionException {
    Map<String, Object> m = new HashMap<>();
    for (PropertyDescriptor pd : Introspector.getBeanInfo(object.getClass())
        .getPropertyDescriptors()) {
      if (pd.getReadMethod() != null && pd.getPropertyType() != java.lang.Class.class) {
        Object result = null;
        try {
          result = pd.getReadMethod().invoke(object, new Object[] {});
        } catch (Exception e) {
        }
        if (result != null) {
          m.put(pd.getName(), result);
        }
      }
    }

    return m;
  }
}
