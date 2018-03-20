package com.fangxuele.spiderproject.cache;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Redis数据库的操作service
 *
 * @author zgzhou 오빠
 * @Date 2016年12月25日
 * @Description 为各个模块提供redis缓存服务的各种方法
 */
@Component
public class RedisCacheService {

    // private static Logger logger =
    // LoggerFactory.getLogger(RedisCacheService.class);

    private final static String prefix = "lol";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private static Jedis jedis;

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: getBytesByString
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public byte[] getBytesByString(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> srs = redisTemplate.getStringSerializer();
                return connection.get(srs.serialize(getKey(key)));
            }
        });
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: setBytesByString
     * @Description: 保存缓存
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Boolean setBytesByString(final String key, final byte[] value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> srs = redisTemplate.getStringSerializer();
                connection.set(srs.serialize(getKey(key)), value);
                return true;
            }
        });
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: getValueByString
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public String getValueByString(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.opsForValue().get(getKey(key));
    }

    /**
     * 不带前缀
     *
     * @param key
     * @return
     */
    public String getValueByStringWithNoPrefix(String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: setValueByString
     * @Description: 保存缓存
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void setValueByString(final String key, final String value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        redisTemplate.opsForValue().set(getKey(key), value);
    }

    /**
     * 不带前缀
     *
     * @param key
     * @param value
     */
    public void setValueByStringWithNoPrefix(String key, String value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: incrByValueByString
     * @Description: 将指定的值递增value
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Long incrByValueByString(final String key, final long value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForValue().increment(getKey(key), value);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: incrByValueByString
     * @Description: 将指定的值递增value
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Double incrByValueByString(final String key, final double value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForValue().increment(getKey(key), value);
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: getValueByList
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public String getValueByList(final String key, final long index) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(index, "index值为空，请检查index值信息");

        Object result = redisTemplate.opsForList().index(getKey(key), index);
        if (result == null) {
            return StringUtils.EMPTY;
        } else {
            return result.toString();
        }
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: popLeftValueByList
     * @Description: 左弹出缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zhouy
     */
    public String popLeftValueByList(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");

        String result = redisTemplate.opsForList().leftPop(getKey(key));
        return result;
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: popRightValueByList
     * @Description: 左弹出缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zhouy
     */
    public String popRightValueByList(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");

        String result = redisTemplate.opsForList().rightPop(getKey(key));
        return result;
    }

    /**
     * @param key 存储的key值
     * @return
     * @throws Exception
     * @Title: getValueOfStartEndByList
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public List<String> getValueOfRangeByList(final String key, final long start, final long end) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(start, "start值为空，请检查start值信息");
        Assert.notNull(end, "end值为空，请检查end值信息");

        return redisTemplate.opsForList().range(getKey(key), start, end);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: leftPushAllCacheByList
     * @Description: 基于List接口保存缓存，从左端（前端）添加
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Long leftPushAllValueByList(final String key, final String... value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForList().leftPushAll(getKey(key), value);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: rightPushAllCacheByList
     * @Description: 基于List接口保存缓存，从右端（后端）添加
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Long rightPushAllValueByList(final String key, final String... value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForList().rightPushAll(getKey(key), value);
    }

    /**
     * @param key key的值
     * @Title: removeCacheByHash
     * @Description: 删除缓存内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long removeValueByList(final String key, final long i, final Object value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(i, "i值为空，请检查i值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForList().remove(getKey(key), i, value);
    }

    /**
     * @param moudle 存储的modle
     * @return 所有KEY值SET
     * @throws Exception
     * @Title: getValue
     * @Description: 根据传入的缓存模块名获取模块名下的所有KEY值SET
     * @author zgzhou
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<String> getKeySetByHash(final String moudle) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Set<String> result = hashOps.keys(getKey(moudle));
        return result;
    }

    /**
     * @param key    存储的key值
     * @param moudle 存储的modle
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueByHash
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public String getValueByHash(final String moudle, final String key) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        Assert.notNull(key, "key值为空，请检查key值信息");

        Object result = redisTemplate.opsForHash().get(getKey(moudle), key);
        if (result == null) {
            return StringUtils.EMPTY;
        } else {
            return result.toString();
        }
    }

    /**
     * @param moudle 存储的modle
     * @return 缓存的数据
     * @throws Exception
     * @Title: getAllValueByHash
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Map<String, String> getAllValueByHash(final String moudle) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries(getKey(moudle));

    }

    /**
     * @param moudle modle模块值
     * @param key    key的值
     * @Title: removeCacheByHash
     * @Description: 删除缓存内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public void removeValueByHash(final String moudle, final String key) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        Assert.notNull(key, "key值为空，请检查key值信息");
        redisTemplate.opsForHash().delete(getKey(moudle), key);
    }

    /**
     * @param moudle modle的值
     * @param key    key的值
     * @param value  需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: incrByValueByHash
     * @Description: 将指定的值递增value
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Long incrByValueByHash(final String moudle, final String key, final long value) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForHash().increment(getKey(moudle), key, value);
    }

    /**
     * @param moudle modle的值
     * @param key    key的值
     * @param value  需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: incrByValueByHash
     * @Description: 将指定的值递增value
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Double incrByValueByHash(final String moudle, final String key, final double value) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForHash().increment(getKey(moudle), key, value);
    }

    /**
     * @param moudle modle的值
     * @param key    key的值
     * @param value  需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: putCache
     * @Description: 保存缓存
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void putValueByHash(final String moudle, final String key, final String value) {
        Assert.notNull(moudle, "moudle值为空，请检查moudle值信息");
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        redisTemplate.opsForHash().put(getKey(moudle), key, value);
    }

    /**
     * @param key key的值
     * @return 保存成功返回ture 失败返回false
     * @Title: putAllValueByHash
     * @Description: 保存缓存
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void putAllValueByHash(final String key, final Map<String, String> values) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(values, "values值为空，请检查values值信息");
        redisTemplate.opsForHash().putAll(getKey(key), values);
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueBySet
     * @Description: 从Set集合中随机返回一个元素
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public String getValueBySet(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.opsForSet().randomMember(getKey(key));
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValuesBySet
     * @Description: 从Set集合中随机返回count个元素
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public List<String> getValuesBySet(final String key, long count) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(count, "count值为空，请检查count值信息");
        return redisTemplate.opsForSet().randomMembers(getKey(key), count);
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValuesBySet
     * @Description: 从Set集合中所有的值
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<String> getValuesBySet(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.opsForSet().members(getKey(key));
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: addValuesBySet
     * @Description: 向Set集合中添加values元素
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Long addValuesBySet(final String key, String... values) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(values, "values值为空，请检查values值信息");
        return redisTemplate.opsForSet().add(getKey(key), values);
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: removeValuesBySet
     * @Description: 向Set集合中删除values元素
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Long removeValuesBySet(final String key, String... values) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(values, "values值为空，请检查values值信息");
        return redisTemplate.opsForSet().remove(getKey(key), values);
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: isContainValueBySet
     * @Description: 判断Set集合中是否包含value元素
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Boolean isContainValueBySet(final String key, String value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForSet().isMember(getKey(key), value);
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueOfStartEndByZSet
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<String> getValueOfRangeByZSet(final String key, final long start, final long end, int sore) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(start, "start值为空，请检查start值信息");
        Assert.notNull(end, "end值为空，请检查end值信息");
        if (sore == RedisCacheConstant.ASC) {
            return redisTemplate.opsForZSet().range(getKey(key), start, end);
        } else {
            return redisTemplate.opsForZSet().reverseRange(getKey(key), start, end);
        }
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueOfScoreByZSet
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<String> getValueOfScoreByZSet(final String key, final double min, final double max, int sore) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(min, "min值为空，请检查min值信息");
        Assert.notNull(max, "max值为空，请检查max值信息");
        if (sore == RedisCacheConstant.ASC) {
            return redisTemplate.opsForZSet().rangeByScore(getKey(key), min, max);
        } else {
            return redisTemplate.opsForZSet().reverseRangeByScore(getKey(key), min, max);
        }
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueAndScoreOfStartEndByZSet
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<FxlTypedTuple> getValueAndScoreOfRangeByZSet(final String key, final long start, final long end,
                                                            int sore) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(start, "start值为空，请检查start值信息");
        Assert.notNull(end, "end值为空，请检查end值信息");

        Set<TypedTuple<String>> resultSet = null;

        Set<FxlTypedTuple> returnSet = Sets.newHashSet();
        if (sore == RedisCacheConstant.ASC) {
            resultSet = redisTemplate.opsForZSet().rangeWithScores(getKey(key), start, end);
        } else {
            resultSet = redisTemplate.opsForZSet().reverseRangeWithScores(getKey(key), start, end);
        }

        FxlTypedTuple fxlTypedTuple = null;
        for (ZSetOperations.TypedTuple<String> typedTuple : resultSet) {
            fxlTypedTuple = new FxlTypedTuple();
            fxlTypedTuple.setScore(typedTuple.getScore());
            fxlTypedTuple.setValue(typedTuple.getValue());
            returnSet.add(fxlTypedTuple);
        }

        return returnSet;
    }

    /**
     * @param key 存储的key值
     * @return 缓存的数据
     * @throws Exception
     * @Title: getValueAndScoreOfScoreByZSet
     * @Description: 查询缓存中key对应的value 存储结构默认为
     * fxl{order:{key1:value1,key2:value2},order2:{}}
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:49:08
     */
    public LinkedHashSet<FxlTypedTuple> getValueAndScoreOfScoreByZSet(final String key, final double min, final double max,
                                                                      int sore) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(min, "min值为空，请检查min值信息");
        Assert.notNull(max, "max值为空，请检查max值信息");
        Set<TypedTuple<String>> resultSet;

        LinkedHashSet<FxlTypedTuple> returnSet = Sets.newLinkedHashSet();
        if (sore == RedisCacheConstant.ASC) {
            resultSet = redisTemplate.opsForZSet().rangeByScoreWithScores(getKey(key), min, max);
        } else {
            resultSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(getKey(key), min, max);
        }

        FxlTypedTuple fxlTypedTuple;
        Iterator<TypedTuple<String>> it = resultSet.iterator();
        while (it.hasNext()) {
            fxlTypedTuple = new FxlTypedTuple();
            TypedTuple<String> typedTuple = it.next();
            fxlTypedTuple.setScore(typedTuple.getScore());
            fxlTypedTuple.setValue(typedTuple.getValue());
            returnSet.add(fxlTypedTuple);
        }

        return returnSet;
    }

    /**
     * @param value value模块值
     * @param key   key的值
     * @Title: removeValueByZSet
     * @Description: 删除缓存内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long removeValueByZSet(final String key, final String... value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForZSet().remove(getKey(key), value);
    }

    /**
     * @param key key的值
     * @Title: removeValueOfStartEndByZSet
     * @Description: 删除缓存内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long removeValueOfRangeByZSet(final String key, final long start, final long end) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(start, "start值为空，请检查start值信息");
        Assert.notNull(end, "end值为空，请检查end值信息");
        return redisTemplate.opsForZSet().removeRange(getKey(key), start, end);
    }

    /**
     * @param key key的值
     * @Title: removeValueOfScoreByZSet
     * @Description: 删除缓存内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long removeValueOfScoreByZSet(final String key, final double min, final double max) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(min, "min值为空，请检查min值信息");
        Assert.notNull(max, "max值为空，请检查max值信息");
        return redisTemplate.opsForZSet().removeRangeByScore(getKey(key), min, max);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @param score 缓存值的排序score
     * @return 保存成功返回ture 失败返回false
     * @Title: addValueAndScoreByZSet
     * @Description: 保存缓存
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void addValueAndScoreByZSet(final String key, final String value, final double score) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        Assert.notNull(score, "score值为空，请检查score值信息");
        redisTemplate.opsForZSet().add(getKey(key), value, score);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @param score 缓存值的排序score
     * @return 保存成功返回ture 失败返回false
     * @Title: incrScoreByZSet
     * @Description: 将ZSet中的value的分值进行相加score值
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Double incrScoreByZSet(final String key, final String value, final double score) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        Assert.notNull(score, "score值为空，请检查score值信息");
        return redisTemplate.opsForZSet().incrementScore(getKey(key), value, score);
    }

    /**
     * @param key   key的值
     * @param value 需要缓存的value
     * @return 保存成功返回ture 失败返回false
     * @Title: getScoreByZSet
     * @Description: 从ZSet集合中获取value对应的分值
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Double getScoreByZSet(final String key, final String value) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(value, "value值为空，请检查value值信息");
        return redisTemplate.opsForZSet().score(getKey(key), value);
    }

    /**
     * @Title: interZSet
     * @Description: 对多个有序集合进行交集运算到目标集合的方法
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long interZSet(final String destKey, final RedisZSetCommands.Aggregate aggregate, final String key1,
                          final String key2) {
        Assert.notNull(destKey, "destKey值为空，请检查destKey值信息");
        Assert.notNull(key1, "key1值为空，请检查key1值信息");
        Assert.notNull(key2, "key2值为空，请检查key2值信息");
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> srs = redisTemplate.getStringSerializer();
                int[] weights = {1, 1};
                return connection.zInterStore(srs.serialize(getKey(destKey)), aggregate, weights,
                        srs.serialize(getKey(key1)), srs.serialize(getKey(key2)));
            }
        });
    }

    /**
     * @Title: interZSet
     * @Description: 对多个有序集合进行差集运算到目标集合的方法
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long differSet(final String destKey, final String key1, final String key2) {
        Assert.notNull(destKey, "destKey值为空，请检查destKey值信息");
        Assert.notNull(key1, "key1值为空，请检查key1值信息");
        Assert.notNull(key2, "key2值为空，请检查key2值信息");
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> srs = redisTemplate.getStringSerializer();
                int[] weights = {1, 1};
                return connection.sDiffStore(srs.serialize(getKey(destKey)), srs.serialize(getKey(key1)), srs.serialize(getKey(key2)));
            }
        });
    }

    /**
     * @return 保存成功返回ture 失败返回false
     * @Title: isExistsKey
     * @Description: 设置缓存中KEY是否存在
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public Boolean isExistsKey(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        return redisTemplate.hasKey(getKey(key));
    }

    /**
     * @param expire_seconds 该缓存的有效期（秒数）
     * @return 保存成功返回ture 失败返回false
     * @Title: setExpireByMoudle
     * @Description: 设置KEY有效时间
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void setExpireByKey(final String key, final long expire_seconds) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(expire_seconds, "expire_seconds值为空，请检查expire_seconds值信息");
        redisTemplate.expire(getKey(key), expire_seconds, TimeUnit.SECONDS);
    }

    /**
     * 不带前缀
     *
     * @param key
     * @param expire_seconds
     */
    public void setExpireByKeyWithNoPrefix(String key, long expire_seconds) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(expire_seconds, "expire_seconds值为空，请检查expire_seconds值信息");
        redisTemplate.expire(key, expire_seconds, TimeUnit.SECONDS);
    }

    /**
     * @param key        key的值
     * @param expireDate 该缓存的到期时间
     * @return 保存成功返回ture 失败返回false
     * @Title: setValueAndExpireByString
     * @Description: 设置KEY的到期时间
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void setExpireAtByKey(final String key, final Date expireDate) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        Assert.notNull(expireDate, "expireDate值为空，请检查expireDate值信息");
        redisTemplate.expireAt(getKey(key), expireDate);
    }

    /**
     * @Title: persistByKey
     * @Description: 取消KEY有效时间，变成永久的KEY
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:54:27
     */
    public void persistByKey(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        redisTemplate.persist(getKey(key));
    }

    /**
     * @param pattern 存储的pattern
     * @return 所有KEY值SET
     * @throws Exception
     * @Title: getKeySet
     * @Description: 根据传入的缓存模块名获取模块名下的所有KEY值SET
     * @author zgzhou
     * @Date 2016年11月14日 下午1:49:08
     */
    public Set<String> getKeySet(final String pattern) {
        Assert.notNull(pattern, "moudle值为空，请检查moudle值信息");
        return redisTemplate.keys(pattern);
    }

    /**
     * @param key key模块值
     * @Title: getSizeByMoudel
     * @Description: 删除缓存模块名的全部内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public Long getSizeByMoudel(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        String moudleKey = getKey(key);
        DataType dt = redisTemplate.type(moudleKey);
        Long size = null;
        switch (dt) {
            case NONE: {
                size = 0L;
                break;
            }
            case STRING: {
                size = redisTemplate.opsForValue().size(moudleKey);
                break;
            }
            case LIST: {
                size = redisTemplate.opsForList().size(moudleKey);
                break;
            }
            case HASH: {
                size = redisTemplate.opsForHash().size(moudleKey);
                break;
            }
            case SET: {
                size = redisTemplate.opsForSet().size(moudleKey);
                break;
            }
            case ZSET: {
                size = redisTemplate.opsForZSet().size(moudleKey);
                break;
            }
            default:
                break;
        }
        return size;
    }

    /**
     * @param key key模块值
     * @Title: removeKeyByCache
     * @Description: 删除缓存模块名的全部内容
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:53:43
     */
    public void removeKeyByCache(final String key) {
        Assert.notNull(key, "key值为空，请检查key值信息");
        redisTemplate.delete(getKey(key));
    }

    /**
     * @return 系统前缀+模块值
     * @Title: getHKey
     * @Description: 添加系统前缀
     * @author zgzhou 오빠
     * @Date 2016年11月14日 下午1:55:58
     */
    private String getKey(final String key) {
        return new StringBuilder().append(prefix).append(RedisCacheConstant.KeySeparator).append(key).toString();
    }

}