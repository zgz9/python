package demo.oracle.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleTypes;

import org.junit.Test;

/*
 * ˼��:
 *
 * 1. û�л��ڱ�׼����ͽӿ�??
 * 2. ����Ƿ�close��
 */
public class TestOracle {
/*
 * create or replace procedure queryEmpInfo(eno in number,
                                         pename out varchar2,
                                         pjob  out varchar2,
                                         psal  out number)
 */
	@Test
	public void testProcedure(){
		//{call <procedure-name>[(<arg1>,<arg2>, ...)]}
		String sql = "{call queryEmpInfo(?,?,?,?)}";
		Connection conn = null;
		CallableStatement call = null;
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			//��ֵ
			call.setInt(1, 7839);
			
			//����out��������Ҫ����
			call.registerOutParameter(2, OracleTypes.VARCHAR);
			call.registerOutParameter(3, OracleTypes.VARCHAR);
			call.registerOutParameter(4, OracleTypes.NUMBER);
			
			//ִ��
			call.execute();
			
			//ȡ�����
			String name = call.getString(2);
			String job = call.getString(3);
			double sal = call.getDouble(4);
			System.out.println(name);
			System.out.println(job);
			System.out.println(sal);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, null);
		}
	}

/*
 * create or replace function queryEmpIncome(eno in number)
return number	
 */
	@Test
	public void testFunction(){
		//{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
		String sql = "{?=call queryEmpIncome(?)}";
		
		Connection conn = null;
		CallableStatement call = null;
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			call.registerOutParameter(1, OracleTypes.NUMBER);
			call.setInt(2, 7839);
			
			call.execute();
			
			//ȡ��������
			double income = call.getDouble(1);
			System.out.println(income);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, null);
		}
	}	

	@Test
	public void testCursor(){
		String sql = "{call MYPACKAGE.queryEmpList(?,?)}";
		
		Connection conn = null;
		CallableStatement call = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			//��ֵ ���ź�
			call.setInt(1, 20);
			
			//����
			call.registerOutParameter(2, OracleTypes.CURSOR);
			
			//ִ�е���
			call.execute();
			
			//ȡ�����
			//*** ǿ������ת��
			rs = ((OracleCallableStatement)call).getCursor(2);
			while(rs.next()){
				String name = rs.getString("ename");
				double sal = rs.getDouble("sal");
				System.out.println(name+"��нˮ��"+sal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, rs);
		}
	}
}














