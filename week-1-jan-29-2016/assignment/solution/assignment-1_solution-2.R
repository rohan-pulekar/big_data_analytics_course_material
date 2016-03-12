setwd("~/work/big-data-analytics-harvard/week1-Jan-29-2016/assignment");
power.consumption.dataframe <- read.delim("2006Data.csv", sep=",");
plot(power.consumption.dataframe$Temperature, power.consumption.dataframe$Power, xlab="Temperature", ylab="Power Consumption");
plot(power.consumption.dataframe$Hour, power.consumption.dataframe$Power, xlab="Hour of the day", ylab="Power Consumption");
boxplot(Power ~ Hour, data=power.consumption.dataframe, ylab="Power Consumption", xlab="Hour of the day");

