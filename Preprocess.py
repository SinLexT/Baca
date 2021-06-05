import os, cv2, numpy as np
from imutils import grab_contours, resize

class PreprocessImage :
    def __init__(self, image) -> None:
        self.image = image
        self.imageCountour = []

    def result(self) -> list:

        image_size = (32,32)

        if self.image.shape[0] > 1300 or self.image.shape[1] > 1300 :
            self.image = self.image_resize(self.image, height = 1200)

        # Convert to grayscale
        grayscaled = cv2.cvtColor(self.image, cv2.COLOR_BGR2GRAY)

        # Blurring the image
        blurred = cv2.GaussianBlur(grayscaled, (3, 5), 0)

        # Apply edge filter to the blurred image
        Cannied = cv2.Canny(blurred, 90, 130)

        # Find contours
        contours = cv2.findContours(Cannied.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        # Grab contours based on opencv version
        contours = grab_contours(contours)




        # Create boundingBox for sorting the contours
        boundingBox = np.array([list(cv2.boundingRect(c)) for c in contours])
        max_height = np.max(boundingBox[:,3])
        nearest = max_height * 1.4

        # Counturs sorting algorithm
        boundingBox = [cv2.boundingRect(c) for c in contours]
        contours, _ = zip(*sorted(zip(contours, boundingBox), key=lambda x: [int(nearest * round(float(x[1][1]) / nearest)), x[1][0]]))

        for contour in contours :
            (x,y,w,h) = cv2.boundingRect(contour)


            #attemp to ignore small contours
            if (w > 15) and (h > 15):
                #create an Region of Interest
                roi = grayscaled[y:y+h, x:x+w]
                
                
                bin_img = cv2.threshold(roi, 0 ,255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
                (iH, iW) = bin_img.shape
                if iW > iH:
                    bin_img = resize(bin_img, width = image_size[0])
                else:
                    bin_img = resize(bin_img, height = image_size[1])


                (iH, iW) = bin_img.shape
                dX = int(max(0, 32 - iW) / 2.0)
                dY = int(max(0, 32 - iH) / 2.0)
                padded = cv2.copyMakeBorder(bin_img, top=dY, bottom=dY,
                        left=dX, right=dX, borderType=cv2.BORDER_CONSTANT,
                        value=(0, 0, 0))
                padded = cv2.resize(padded, image_size)

                #normalize the image so that it got the same value of testImg
                padded = padded.astype("float32") / 255.0
                padded = np.expand_dims(padded, axis=-1)
                
                #put it into list of the queue of images to predict
                self.imageCountour.append([padded, (x, y, w, h)])

        #get each location and image of detected char
        self.imageCountour = np.array([c[0] for c in self.imageCountour], dtype="float32")

        return self.imageCountour

    def image_resize(self, image, width = None, height = None, inter = cv2.INTER_AREA):
        # initialize the dimensions of the image to be resized and
        # grab the image size
        dim = None
        (h, w) = image.shape[:2]

        # if both the width and height are None, then return the
        # original image
        if width is None and height is None:
            return image

        # check to see if the width is None
        if width is None:
            # calculate the ratio of the height and construct the
            # dimensions
            r = height / float(h)
            dim = (int(w * r), height)

        # otherwise, the height is None
        else:
            # calculate the ratio of the width and construct the
            # dimensions
            r = width / float(w)
            dim = (width, int(h * r))

        # resize the image
        resized = cv2.resize(image, dim, interpolation = inter)

        # return the resized image
        return resized

class RequestToImage :
    def __init__(self, file) -> None:
        self.file = file

    def result(self) :
        npimg = np.fromstring(self.file, np.uint8)
        img = cv2.imdecode(npimg,cv2.IMREAD_COLOR)
        return img