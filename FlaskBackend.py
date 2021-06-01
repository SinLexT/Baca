from flask import Flask, request
from Preprocess import PreprocessImage, RequestToImage
from AIProcessor import WrappedTF

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def welcome():
    return "Hello Flask!"

@app.route('/test', methods=['POST'])
def predict():
    file = request.files['image'].read()
    requestToImage = RequestToImage(file)
    img = requestToImage.result()

    preprocess = PreprocessImage(img)
    imageProcessed = preprocess.result()


    aIProcessor = WrappedTF(imageProcessed)

    # return send_file(filename, mimetype='image/gif')
    return aIProcessor.predict()

if __name__ == "__main__":
    # app.run()
    app.run(debug=True, host='0.0.0.0', port='5000')