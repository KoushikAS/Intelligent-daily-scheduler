import tensorflow as tf
import csv
import pandas as pd

CSV_COLUMN_NAMES = ['Free','hour','minu','weekday']

def load_data(label_name='Free'):
    #Parses the csv file 
    
    
    train_path="free_time.csv"
    # Parse the local CSV file.
    train = pd.read_csv(filepath_or_buffer=train_path,
                        names=CSV_COLUMN_NAMES,  # list of column names
                        header=0,  # ignore the first row of the CSV file.
                        skipinitialspace=True
                       )
    
    #To convert Everything to str dtype 
    train.hour = train.hour.astype(str)
    train.minu= train.minu.astype(str)
    train.weekday= train.weekday.astype(str)
    

    # train now holds a pandas DataFrame, which is data structure
    # analogous to a table.
    
    # 1. Assign the DataFrame's labels (the right-most column) to train_label.
    # 2. Delete (pop) the labels from the DataFrame.
    # 3. Assign the remainder of the DataFrame to train_features
    train_features, train_label = train, train.pop(label_name)

    return (train_features, train_label)



def train_input_fn(features, labels, batch_size):
        dataset = tf.data.Dataset.from_tensor_slices((dict(features), labels))
        
        dataset = dataset.shuffle(buffer_size=1000).repeat(count=None).batch(batch_size)
        
        return dataset.make_one_shot_iterator().get_next()


def eval_input_fn(features, labels=None, batch_size=None):
    """An input function for evaluation or prediction"""
    if labels is None:
        # No labels, use only features.
        inputs = features
    else:
        inputs = (features, labels)

    # Convert inputs to a tf.dataset object.
    dataset = tf.data.Dataset.from_tensor_slices(inputs)

    # Batch the examples
    assert batch_size is not None, "batch_size must not be None"
    dataset = dataset.batch(batch_size)

    # Return the read end of the pipeline.
    return dataset.make_one_shot_iterator().get_next()



def NNModel():

    (train_feature, train_label) = load_data()

    weekday = tf.feature_column.categorical_column_with_vocabulary_list('weekday', ['0', '1', '2', '3', '4','5','6'])
    hour = tf.feature_column.categorical_column_with_vocabulary_list('hour', [ '0', '1', '2', '3', '4','5','6','7','8','10','12','13','14','15','16','17','18','19','20','21','22','23'])
    minu = tf.feature_column.categorical_column_with_vocabulary_list('minu', [ '0', '15', '30', '45'])


    weekday_x_hour_x_minu = tf.feature_column.crossed_column(['weekday', 'hour','minu'], hash_bucket_size=1000)
    weekday_x_hour = tf.feature_column.crossed_column(['weekday', 'hour'], hash_bucket_size=1000)
    hour_x_minu = tf.feature_column.crossed_column(['hour','minu'], hash_bucket_size=1000)


    base_columns = [
        tf.feature_column.indicator_column(weekday),
        tf.feature_column.indicator_column(hour),
        tf.feature_column.indicator_column(minu)
    ]

    crossed_columns = [
         tf.feature_column.indicator_column(weekday_x_hour_x_minu),
         tf.feature_column.indicator_column(weekday_x_hour) , 
         tf.feature_column.indicator_column(hour_x_minu)
    ]

    classifier = tf.estimator.DNNClassifier(feature_columns=base_columns+crossed_columns,hidden_units=[6, 4,2],n_classes=2)


    classifier.train(input_fn=lambda:train_input_fn(train_feature, train_label, 50 ),steps=1000)



    df=pd.read_csv("topredict.csv",parse_dates=True)

    predict_x= {  'hour':[ ],'minu':[ ] ,'weekday':[ ]}

    predict_x['hour'] = df.hour.astype(str)
    predict_x['minu']= df.minu.astype(str)
    predict_x['weekday']= df.weekday.astype(str)
    df['Free'] = [0 for x in range(df.shape[0])]


    predictions = classifier.predict(
        input_fn=lambda:eval_input_fn(predict_x,
                                  labels=None,
                                  batch_size=50))
    print(predictions)

    i=0
    for pred_dict in zip(predictions):
        if pred_dict[0]['classes'] == [b'1']:
        
            df.iloc[i,0]=1
        
        i=i+1

    print(df.head())

    df.to_csv("predictedValue.csv",mode = 'w', index=False)
