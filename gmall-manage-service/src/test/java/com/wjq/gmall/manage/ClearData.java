package com.wjq.gmall.manage;

import java.sql.*;

public class ClearData {
    public static void main(String[] args) throws SQLException {


        Connection connection = JobStorage.initMysql();

        String sql = "select working_life,salary,position," +
                "title,staff_numbers,company_type,company_name,"+
                "education,financing_info,recruiter_job,job_type,job_id"+

                " from job_without_repeat";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            double avg_working_life=0;
            int min_working_life=0;
            int max_working_life=0;

            double max_salary =0;
            double min_salary =0;
            double avg_salary=0;

            String position1 = "";
            String position2 = "";
            String position3 = "";
            String working_life = resultSet.getString(1);

            if(working_life.contains("-")){
                String[] workinglifes = working_life.split("年")[0].split("-");
                avg_working_life = (Double.valueOf(workinglifes[0])+Integer.valueOf(workinglifes[1]))/2;
                min_working_life= Integer.valueOf(workinglifes[0]);
                max_working_life = Integer.valueOf(workinglifes[1]);
            }

            String salary = resultSet.getString(2);
            if(salary.contains("薪")){//80-110K·14薪
                String[] salarys = salary.split("·");
                int xin = Integer.valueOf(salarys[1].split("薪")[0]);
                 max_salary = Double.valueOf(salarys[0].split("-")[1].split("K")[0])*1000*xin/12;
                 min_salary = Double.valueOf(salarys[0].split("-")[0])*1000*xin/12;
                 avg_salary = (max_salary+min_salary);

            }else if(salary.contains("天")){
                // 80-130/天
                String s = salary.split("/")[0];
                String[] ss = s.split("-");
                 max_salary = Double.valueOf(ss[1])*22;
                 min_salary = Double.valueOf(ss[0])*22;
                 avg_salary = (max_salary+min_salary)/2;
            }else{//80-150K
                 min_salary =  Double.valueOf(salary.split("-")[0]);
                 max_salary = Double.valueOf(salary.split("-")[1].split("K")[0]);
                 avg_salary = (max_salary+min_salary)/2;
            }

            String position = resultSet.getString(3);
            String[] positions = position.split(" ");
            if(positions.length==1){
                 position1 = positions[0];
                 position2 = "";
                 position3 = "";
            }else if(positions.length==2){
                 position1 = positions[0];
                 position2 = positions[1];
                 position3 = "";
            }else if(positions.length==3){
                 position1 = positions[0];
                 position2 = positions[1];
                 position3 = positions[2];

            }else{
                System.out.println("这句话不会出现吧");
            }


            String title = resultSet.getString(4);

            String staff_number = resultSet.getString(5);

            String min_staff_number = staff_number;

            String max_staff_number = staff_number;

            String avg_staff_number = staff_number;

            String company_type = resultSet.getString(6);

            String company_name = resultSet.getString(7);

            String education = resultSet.getString(8);

            String financing_info = resultSet.getString(9);

            String recruiter_job = resultSet.getString(10);

            String job_type = resultSet.getString(11);

            Statement statement = connection.createStatement();

            String sql2 = "insert into job_cleared (" +
                    "min_working_life,max_working_life,avg_working_life," +
                    "title," +
                    "avg_staff_number,min_staff_number,max_staff_number," +
                    "max_salary,min_salary,avg_salary," +
                    "company_type," +
                    "education," +
                    "financing_info" +
                    ",company_name," +
                    "recruiter_job," +
                    "job_type," +
                    "position1,position2,position3) values (" +
                    "\'"+min_working_life+"\'"+","+
                    "\'"+max_working_life+"\'"+","+
                    "\'"+avg_working_life+"\'"+","+
                    "\'"+title+"\'"+","+
                    "\'"+avg_staff_number+"\'"+","+
                    "\'"+min_staff_number+"\'"+","+
                    "\'"+max_staff_number+"\'"+","+
                    "\'"+max_salary+"\'"+","+
                    "\'"+min_salary+"\'"+","+
                    "\'"+avg_salary+"\'"+","+
                    "\'"+company_type+"\'"+","+
                    "\'"+education+"\'"+","+
                    "\'"+financing_info+"\'"+","+
                    "\'"+company_name+"\'"+","+
                    "\'"+recruiter_job+"\'"+","+
                    "\'"+job_type+"\'"+","+
                    "\'"+position1+"\'"+","+
                    "\'"+position2+"\'"+","+
                    "\'"+position3+"\'"
                    +")";

            System.out.println(sql2);
            statement.executeUpdate(sql2);
        }
    }

}
