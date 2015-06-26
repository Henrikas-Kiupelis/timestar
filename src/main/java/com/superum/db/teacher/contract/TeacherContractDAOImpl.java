package com.superum.db.teacher.contract;

import static com.superum.db.generated.timestar.Tables.TEACHER_CONTRACT;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.exception.DatabaseException;

@Repository
@Transactional
public class TeacherContractDAOImpl implements TeacherContractDAO {

	@Override
	public TeacherContract create(TeacherContract contract) {
		int id = contract.getId();
		byte paymentDay = contract.getPaymentDay();
		
		int createResult = sql.insertInto(TEACHER_CONTRACT)
				.set(TEACHER_CONTRACT.TEACHER_ID, contract.getId())
				.set(TEACHER_CONTRACT.PAYMENT_DAY, paymentDay)
				.execute();
		if (createResult == 0)
			throw new DatabaseException("Couldn't insert contract: " + contract);
		
		return new TeacherContract(id, paymentDay);
	}

	@Override
	public TeacherContract read(Integer id) {
		return sql.selectFrom(TEACHER_CONTRACT)
				.where(TEACHER_CONTRACT.TEACHER_ID.eq(id))
				.fetch().stream()
				.findFirst()
				.map(TeacherContract::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find contract with ID: " + id));
	}

	@Override
	public TeacherContract update(TeacherContract contract) {
		int id = contract.getId();

		TeacherContract old = read(id);
		
		sql.update(TEACHER_CONTRACT)
			.set(TEACHER_CONTRACT.PAYMENT_DAY, contract.getPaymentDay())
			.where(TEACHER_CONTRACT.TEACHER_ID.eq(id))
			.execute();
		
		return old;
	}

	@Override
	public TeacherContract delete(Integer id) {
		TeacherContract old = read(id);
		
		int result = sql.delete(TEACHER_CONTRACT)
				.where(TEACHER_CONTRACT.TEACHER_ID.eq(id))
				.execute();
		if (result == 0)
			throw new DatabaseException("Couldn't delete contract with ID: " + id);
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherContractDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
