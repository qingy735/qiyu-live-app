package org.qiyu.live.common.interfaces.utils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: QingY
 * @Date: Created in 22:11 2024-04-07
 * @Description:
 */
public class ConvertBeanUtils {

    /**
     * 将一个对象转换为目标对象
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T target = newInstance(targetClass);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <K, T> List<T> convertList(List<K> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        ArrayList<T> targetList = new ArrayList<>((int) (sourceList.size() / 0.75) + 1);
        for (K k : sourceList) {
            targetList.add(convert(k, targetClass));
        }
        return targetList;
    }

    private static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (Exception e) {
            throw new BeanInstantiationException(targetClass, "instantiation error", e);
        }
    }
}
