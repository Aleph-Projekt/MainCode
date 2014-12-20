EU.df <- read.csv("/myinput.csv",header=T)
EU.xts <- xts(EU.df[,2:5],seq(as.Date("1970-01-02"),len=nrow(EU.df),by="day"))
write.csv(EU.df, file = "/myoutputfile.csv",row.names=FALSE)