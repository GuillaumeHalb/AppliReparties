ps ax | grep "java" | egrep -v "eclipse" | cut -b1-06 | xargs -t kill
