package app.estateagency.services;

import app.estateagency.jpa.entities.CreditworthinessInfo;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static org.apache.spark.sql.functions.avg;

@Service
public class CreditWorthinessAssessmentService {
    @Value("${app.credit-assessment-path}")
    private String csvPath;

    public Double assess(CreditworthinessInfo creditworthinessInfo) {
        SparkConf conf = new SparkConf();
        conf.set("spark.driver.extraJavaOptions", "-Dio.netty.tryReflectionSetAccessible=true");
        conf.set("spark.executor.extraJavaOptions", "-Dio.netty.tryReflectionSetAccessible=true");

        SparkSession spark = SparkSession.builder()
                .appName("Credit Assessment")
                .config("spark.master", "local")
                .config("spark.eventLog.enabled", false)
                .getOrCreate();

        Dataset<Row> df = spark.read().option("header", true).csv(csvPath);

        Dataset<Row> result = df.filter(df.col("Income").geq(creditworthinessInfo.getIncome())
                .and(df.col("Age").geq(creditworthinessInfo.getAge()))
                .and(df.col("Education Level").equalTo(creditworthinessInfo.getEducationLevel().toString())));

        try {
            if (result.count() > 0)
                return result.agg(avg("Credit")).first().getDouble(0);
        } finally {
            spark.stop();
        }

        return Double.NaN;
    }
}
