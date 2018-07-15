import csv
import pandas as pd
from pandas import DataFrame
import datetime
import math

def schedule_algo():
    df=pd.read_csv("tasks.csv",parse_dates=True)
    total_work=0.0
    for i in range(len(df)):
        total_work+=df.iloc[i].hour

    df1=pd.read_csv("predictedValue.csv",parse_dates=True)
    print(df1)
    total_free={
    0:0,
    1:0,
    2:0,
    3:0,
    4:0,
    5:0,
    6:0
    }
    for i in range(len(df1)):
        weekday=df1.iloc[i].weekday
        if(df1.iloc[i].Free):
            total_free[weekday]+=0.25

    print("Total free time according to days")
    print(total_free)

    Free_Time=0
    for i in range(len(total_free)):
        Free_Time+=total_free[i]

    extra_time=math.floor((Free_Time-total_work)/7)
    print("extra time per day")
    print(extra_time)

    df3=df.copy()
    df4 = pd.DataFrame(columns=['name','hour','weekday'])
    for i in range(len(total_free)):
        rem_time=total_free[i]-extra_time
        if(rem_time>0):
            
            j=0
        
            while (rem_time!=0.0 and j<len(df3)):

                temp=min(df3.iloc[j].hour,rem_time)
                if(temp>0):
                    rem_time=rem_time-temp
                      
                    df3.loc[j,"hour"]=df3.iloc[j].hour-temp
            

                    df4 = df4.append({'name': df3.iloc[j].Task, 'hour': temp,'weekday':i}, ignore_index=True)
            
            
                j+=1
    print("Tasks according to days")            
    print(df4)

    df5 = pd.DataFrame(columns=['starth','startm','endh','endm','total','weekday'])
    flag=0

    total=0.0


    for i in range(len(df1)):
        if(df1.iloc[i].Free==1 and flag == 0):
            starth=df1.iloc[i].hour
            startm=df1.iloc[i].minu

            flag=1
    
        if(df1.iloc[i].Free==1 and flag == 1):
            total+=0.25

        try:    
            if((df1.iloc[i].Free==0 and flag == 1) or (df1.iloc[i].weekday != df1.iloc[i-1].weekday and  flag == 1)) :
                endh=df1.iloc[i].hour
                endm=df1.iloc[i].minu
                weekday=df1.iloc[i-1].weekday
                flag=0
                df5 = df5.append({'starth': starth,'startm': startm, 'endh':endh, 'endm':endm,'total': total,'weekday':weekday }, ignore_index=True)
            
                total=0
        except:
            pass
    df5=df5.sort_values(by=['weekday', 'total'], ascending=[1,0])

    df6 = pd.DataFrame(columns=['Task','start','end','weekday'])
    for i in range(len(df4)):
        weekday=df4.iloc[i].weekday
        rem_time=df4.iloc[i].hour
        name=df4.iloc[i]['name']
        j=0
       
        while(rem_time!=0.0 and j<len(df5)):
            if(df5.iloc[j].weekday==weekday):
                temp=min(df5.iloc[j].total,rem_time)
                rem_time=rem_time-temp
                df5.iloc[j].total=df5.iloc[j].total-temp
            
                starth=df5.iloc[j].starth
                startm=df5.iloc[j].startm
                hour=int(temp)
                minu=temp-hour
                if(minu==0.25):
                    minu=15
                if(minu == 0.5):
                    minu=30
                if(minu == 0.75):
                    minu=45
      
                endh=int(starth)+hour
                endm=int(startm)+minu
                if(endm>=60):
                    endh+=1
                    endm-=60
            
                df5.iloc[j].starth=endh
                df5.iloc[j].startm=endm
      
            
            
                start=str(int(starth))+" : "+str(int(startm))
                end=str(int(endh))+" : "+str(int(endm))
                df6 = df6.append({'Task':name,'start': start, 'end':end,'weekday':weekday }, ignore_index=True)
     
            j+=1

    print("final Scheduled tasks")  
    print(df6)
    df6.to_json("scheduled_Task.json")
       


                                                   
                                                   
                                                
        
    
    
            
    
