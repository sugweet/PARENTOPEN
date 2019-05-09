package com.brigeintelligent.base.basemethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
@NoRepositoryBean //提示spring不去实例化该接口
public interface BaseDao<T,ID extends Serializable> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {
}
