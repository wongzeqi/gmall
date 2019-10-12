package com.wjq.gmall.manage;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class JobStorage {
    public static void main(String [] args) throws SQLException, IOException {

        Connection connection = initMysql();
        Statement statement = connection.createStatement();

        File f = new File("D:\\spider_code\\boss\\result");
        List<File> files = Arrays.asList(f.listFiles());
        for(File f2 : files){
            List<File> fileList = Arrays.asList(f2.listFiles());
            for(File f3 : fileList){
                String job_type = f3.getName();
                List<File> fileList2 = Arrays.asList(f3.listFiles());
                for(File f4 : fileList2){
                    System.out.println(f4.getName());
                    //读取文件


                    String encoding="GBK";



                    if(f4.isFile() && f4.exists()) { //判断文件是否存在
                        InputStreamReader read = new InputStreamReader(new FileInputStream(f4), encoding);//考虑到编码格式
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;//每一行的文本内容
                        while ((lineTxt = bufferedReader.readLine()) != null) {

                            // toJavaObject - json字符串转换为java对象
                            Job job = JSONObject.parseObject(lineTxt,Job.class);

                            job.setJob_type(job_type);

                            String sql = "insert into job_orginal (working_life,title,staff_numbers,salary,company_type,education,financing_info" +
                                    ",company_name,position,recruiter_job,job_type) values (" +
                                    "\'"+job.getWorking_life()+"\'"+","+
                                    "\'"+job.getTitle()+"\'"+","+
                                    "\'"+job.getStaff_numbers()+"\'"+","+
                                    "\'"+job.getSalary()+"\'"+","+
                                    "\'"+job.getCompany_type()+"\'"+","+
                                    "\'"+job.getEducation()+"\'"+","+
                                    "\'"+job.getFinancing_info()+"\'"+","+
                                    "\'"+job.getCompany_name()+"\'"+","+
                                    "\'"+job.getPosition()+"\'"+","+
                                    "\'"+job.getRecruiter_job()+"\'"+","+
                                    "\'"+job.getJob_type()+"\'"
                                    +")";


                            System.out.println(sql);

                            statement.executeUpdate(sql);

                        }
                    }

                }
            }

        }



    }


    public static Connection initMysql() {
        Connection conn = null;
        try{
            //jdbc:数据库类型://主机IP:端口/数据库名?characterEncoding=编码
            String user="root";
            String password = "20152154";
            String url="jdbc:mysql://localhost:3306/boss_job?characterEncoding=gbk";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        }catch(Exception e){
            System.out.println("数据库连接异常!");
            e.printStackTrace();
        }
        return conn;
    }




    public class Job {
        private String working_life; //工作年限 经验
        private String title;        //工作名称
        private String staff_numbers;//公司员工数量
        private String salary;       //工资
        private String company_type; //公司类型
        private String education;     //教育背景
        private String financing_info;//金融信息
        private String company_name;//公司名称
        private String position;    //位置
        private String recruiter_job;//招聘者职位
        private String job_type;//工作类型  例如：算法 开发 java等

        public String getJob_type() {
            return job_type;
        }

        public void setJob_type(String job_type) {
            this.job_type = job_type;
        }

        public String getWorking_life() {
            return working_life;
        }

        public void setWorking_life(String working_life) {
            this.working_life = working_life;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStaff_numbers() {
            return staff_numbers;
        }

        public void setStaff_numbers(String staff_numbers) {
            this.staff_numbers = staff_numbers;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getCompany_type() {
            return company_type;
        }

        public void setCompany_type(String company_type) {
            this.company_type = company_type;
        }



        public String getFinancing_info() {
            return financing_info;
        }

        public void setFinancing_info(String financing_info) {
            this.financing_info = financing_info;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getRecruiter_job() {
            return recruiter_job;
        }

        public void setRecruiter_job(String recruiter_job) {
            this.recruiter_job = recruiter_job;
        }

        public Job() {
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public Job(String working_life, String title, String staff_numbers, String salary, String company_type, String education, String financing_info, String company_name, String position, String recruiter_job) {
            this.working_life = working_life;
            this.title = title;
            this.staff_numbers = staff_numbers;
            this.salary = salary;
            this.company_type = company_type;
            this.education = education;
            this.financing_info = financing_info;
            this.company_name = company_name;
            this.position = position;
            this.recruiter_job = recruiter_job;
        }

        @Override
        public String toString() {
            return "Job{" +
                    "working_life='" + working_life + '\'' +
                    ", title='" + title + '\'' +
                    ", staff_numbers='" + staff_numbers + '\'' +
                    ", salary='" + salary + '\'' +
                    ", company_type='" + company_type + '\'' +
                    ", education='" + education + '\'' +
                    ", financing_info='" + financing_info + '\'' +
                    ", company_name='" + company_name + '\'' +
                    ", position='" + position + '\'' +
                    ", recruiter_job='" + recruiter_job + '\'' +
                    '}';
        }
    }


}
