import numpy as np
import tensorflow as tf


class WrappedTF :
    def __init__(self, imageProcessed) -> None:
        self.imageProcessed = imageProcessed

    def predict(self) :
        tf.keras.backend.clear_session()
        # Load TFLite model and allocate tensors.
        interpreter = tf.lite.Interpreter(model_path="model.tflite")
        
        # Get input and output tensors.
        input_details = interpreter.get_input_details()
        output_details = interpreter.get_output_details()

        interpreter.allocate_tensors()

        output_data = []
        for i in range(len(self.imageProcessed)) :
            interpreter.set_tensor(input_details[0]['index'], [self.imageProcessed[i]])

            # run the inference
            interpreter.invoke()

            # output_details[0]['index'] = the index which provides the input
            output_data.append(interpreter.get_tensor(output_details[0]['index']))


        classLabels = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        output_str =""
        for j in range(len(output_data)) :
            i = np.argmax(output_data[j])
            # prob = max(output_data[j]) # Not yet implmented
            output_str += classLabels[i]

        return output_str