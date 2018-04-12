import tensorflow as tf
import csv
import pandas as pd
from sklearn.model_selection import train_test_split
import numpy as np

df=pd.read_csv("data.csv",parse_dates=True)
print(df.head())

x_axis=df[['month','day','hour','minu','weekday']]
y_axis=df[['Free']]


x_train, x_test, y_train1, y_test1 = train_test_split(x_axis, y_axis, test_size=0.1)

y_train = pd.DataFrame({'0': [], '1': []})
for i  in range(0,len(y_train1)):
    z=[0,0]
    if(y_train1.iloc[i].Free==0):
        z[0]=1
    else:
        z[1]=1
    
    y_train = y_train.append({'0':int(z[0]), '1':int(z[1])}, ignore_index=True)


y_test = pd.DataFrame({'0': [], '1': []})
for i  in range(0,len(y_test1)):
    z=[0,0]
    if(y_test1.iloc[i].Free==0):
        z[0]=1
    else:
        z[1]=1
    
    y_test = y_test.append({'0':int(z[0]), '1':int(z[1])}, ignore_index=True)

print(y_train.head())
print(y_test.head())

n_nodes_hl1 = 512
n_nodes_hl2 = 256
#n_nodes_hl3 = 500

n_classes = 2
batch_size = 100

x = tf.placeholder('float', [None, len(x_train.iloc[0])])
y = tf.placeholder('float',[None, len(y_train.iloc[0])])

def neural_network_model(data):
    hidden_1_layer = {'weights':tf.Variable(tf.random_normal([len(x_train.iloc[0]), n_nodes_hl1])),
                      'biases':tf.Variable(tf.random_normal([n_nodes_hl1]))}

    hidden_2_layer = {'weights':tf.Variable(tf.random_normal([n_nodes_hl1, n_nodes_hl2])),
                      'biases':tf.Variable(tf.random_normal([n_nodes_hl2]))}

    #hidden_3_layer = {'weights':tf.Variable(tf.random_normal([n_nodes_hl2, n_nodes_hl3])),
     #                 'biases':tf.Variable(tf.random_normal([n_nodes_hl3]))}

    output_layer = {'weights':tf.Variable(tf.random_normal([n_nodes_hl2, n_classes])),
                    'biases':tf.Variable(tf.random_normal([n_classes])),}


    l1 = tf.add(tf.matmul(data,hidden_1_layer['weights']), hidden_1_layer['biases'])
    drop_out = tf.nn.dropout(l1, 0.5)
    l1 = tf.nn.relu(l1)

    l2 = tf.add(tf.matmul(l1,hidden_2_layer['weights']), hidden_2_layer['biases'])
    drop_out = tf.nn.dropout(l2, 0.7)    
    l2 = tf.nn.relu(l2)

    #l3 = tf.add(tf.matmul(l2,hidden_3_layer['weights']), hidden_3_layer['biases'])
    #l3 = tf.nn.relu(l3)

    output = tf.matmul(l2,output_layer['weights']) + output_layer['biases']
    
    return output

def train_neural_network(x):
    prediction = neural_network_model(x)
    # OLD VERSION:
    #cost = tf.reduce_mean( tf.nn.softmax_cross_entropy_with_logits(prediction,y) )
    # NEW:
   
    cost = tf.reduce_mean( tf.nn.softmax_cross_entropy_with_logits(logits=prediction, labels=y) )
    optimizer = tf.train.AdamOptimizer().minimize(cost)
    
    hm_epochs = 25
    with tf.Session() as sess:
        # OLD:
        #sess.run(tf.initialize_all_variables())
        # NEW:
        sess.run(tf.global_variables_initializer())

        for epoch in range(hm_epochs):
            epoch_loss = 0
            i=0
            while(i<len(x_train)):
                start=i
                end=i+ batch_size
                
                batch_x=np.array(x_train[start:end])
                batch_y=np.array(y_train[start:end])                
                
                
                _, c = sess.run([optimizer, cost], feed_dict={x:batch_x, y: batch_y})
                epoch_loss += c
                i=i+batch_size
            print('Epoch', epoch, 'completed out of',hm_epochs,'loss:',epoch_loss)

        correct = tf.equal(tf.argmax(prediction, 1), tf.argmax(y, 1))

        accuracy = tf.reduce_mean(tf.cast(correct, 'float'))
        print('Accuracy:',accuracy.eval({x:x_test, y:y_test}))

train_neural_network(x)
