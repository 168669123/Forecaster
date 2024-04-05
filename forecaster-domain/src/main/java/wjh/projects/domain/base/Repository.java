package wjh.projects.domain.base;

public interface Repository<T extends AggregateRoot<ID>, ID extends Identifier> {

    /**
     * 同步信息到数据库中
     */
    void save(T t);
}
