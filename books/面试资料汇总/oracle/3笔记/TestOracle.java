package demo.oracle.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.driver.OracleTypes;

import org.junit.Test;

/*
 * 思考:
 *
 * 1. 没有基于标准的类和接口??
 * 2. 光标是否close？
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
			
			//赋值
			call.setInt(1, 7839);
			
			//对于out参数，需要申明
			call.registerOutParameter(2, OracleTypes.VARCHAR);
			call.registerOutParameter(3, OracleTypes.VARCHAR);
			call.registerOutParameter(4, OracleTypes.NUMBER);
			
			//执行
			call.execute();
			
			//取出结果
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
			
			//取出年收入
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
			
			//赋值 部门号
			call.setInt(1, 20);
			
			//申明
			call.registerOutParameter(2, OracleTypes.CURSOR);
			
			//执行调用
			call.execute();
			
			//取结果集
			//*** 强制类型转换
			rs = ((OracleCallableStatement)call).getCursor(2);
			while(rs.next()){
				String name = rs.getString("ename");
				double sal = rs.getDouble("sal");
				System.out.println(name+"的薪水是"+sal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, rs);
		}
	}
}














