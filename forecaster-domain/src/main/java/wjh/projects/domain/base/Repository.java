package wjh.projects.domain.base;

import java.io.Serializable;

public interface Repository<T extends AggregateRoot<ID>, ID extends Identifier> extends Serializable {

    /**
     * 同步对象信息到数据库中
     */
    void save(T t);

    /**
     * 根据唯一标识符查询对象信息
     */
    T query(ID id);

    /**
     * 根据唯一标识符删除对象信息
     */
    void delete(ID id);
}
