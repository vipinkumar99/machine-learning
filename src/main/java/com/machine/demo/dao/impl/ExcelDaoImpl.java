package com.machine.demo.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.machine.demo.dao.ExcelDao;
import com.machine.demo.entity.ExcelEntity;

@Repository
@Transactional
public class ExcelDaoImpl implements ExcelDao {

	@Autowired
	private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public boolean saveList(List<ExcelEntity> entities) {
		Session session = null;
		// Transaction tx = null;
		try {
			session = getSession();
			// tx = session.beginTransaction();
			for (ExcelEntity entity : entities) {
				session.save(entity);
			}
			// tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// if (tx != null) {
			// tx.rollback();
			// }
			return false;
		} finally {
			// session.close();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExcelEntity> getByColumnName(List<String> columnNames) {
		@SuppressWarnings("rawtypes")
		Query query = getSession().createQuery(" from ExcelEntity where columnName in (:colName)");
		query.setParameterList("colName", columnNames);
		return query.getResultList();
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		@SuppressWarnings("rawtypes")
		Query query = getSession().createQuery("delete from ExcelEntity where id in (:oId)");
		query.setParameterList("oId", ids);
		return query.executeUpdate();
	}

}
