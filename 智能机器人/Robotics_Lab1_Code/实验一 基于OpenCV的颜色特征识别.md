# 实验一 基于OpenCV的颜色特征识别

SA18225293 彭辰铭

### 配置环境与运行

本次实验原则上是可以在win10，linuxOs上进行的。

1、我先在Ubuntu下运行了。采取的是python2.7+opencv3.4.4。

2、在win10下由于采用的是python3.6+opencv3.4.4。所以需要将提供的python2.7 版本代码迁移到python3中来。

我采用的是2to3工具。

主要不兼容的地方在于python3中不再支持函数中写入元组参数解包

使用

```shell
pip install 2to3
//cd 到代码所在目录
2to3 test.py video.py common.py
pip install opencv-python
```

之后确保安装有所需要的包，即可正常运行

若提示缺少包，则可以使用pip工具逐一进行安装。

### 主要代码分析

test.py代码

```python
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import cv2
import serial

# from picamera.array import PiRGBArray
# from picamera import PiCamera
# import time
# import copy
import video
from matplotlib import pyplot as plt

class App(object):
    def __init__(self, color):
        #调用video的create_capture函数创建cam窗口
        self.cam = video.create_capture(0)
#         self.cam = PiCamera()
#         self.cam.resolution = (320,240)
#         self.cam.framerate = 32
#         self.rCa = PiRGBArray(self.cam, size=(320,240))
#         time.sleep(0.1) 
#        self.cam.capture(self.rCa, format='bgr')
#        self.frame = self.rCa.array
#        ret, self.frame = self.cam.read()
		#命名窗口为 camshift
        cv2.namedWindow('camshift')
#color为0 表示抓取红色
        int(color)
        if color == 0:
            print("yes")
            self.roi = cv2.imread( 'hong.jpg' )
            self.flag = "Hong"
#
            self.selection = None
            self.tracking_state = 0
            self.show_backproj = False
#
	    #抓取蓝色
        else:
            print("no")
            self.flag = "Lan"
            self.roi = cv2.imread('lan.jpg')
            self.selection = None
            self.tracking_state = 0
            self.show_backproj = False
#         self.ser = serial.Serial('/dev/ttyAMA0',115200,timeout=0.5)

                    
    def start(self):
        self.tracking_state = 0
        #x, y = np.int16([220, 110]) # BUG
        if self.flag == 'Hong':
            self.selection = (4, 6, 407, 304)
            self.tracking_state = 1
            print("yes")
        else:
            self.selection = (40, 54, 296, 230)
            self.tracking_state = 1
    #        print "start"

    def show_hist(self):
        #显示直方图
        bin_count = self.hist.shape[0]
        bin_w = 24
#初始化256各 bin_conut × 24， 3 的 矩阵  3维数组， 数据类型为np.uint8 
        img = np.zeros((256, bin_count*bin_w, 3), np.uint8)
        for i in range(bin_count):
            h = int(self.hist[i])
            cv2.rectangle(img, (i*bin_w+2, 255), ((i+1)*bin_w-2, 255-h), (int(180.0*i/bin_count), 255, 255), -1)
        img = cv2.cvtColor(img, cv2.COLOR_HSV2BGR)
        cv2.imshow('hist', img)

    def run(self):
        roi = self.roi
        self.start()
        while True:
#         for frame in self.cam.capture_continuous(self.rCa, format='bgr', use_video_port=True):
#读取视频帧
            ret, self.frame = self.cam.read()
#             self.frame = frame.array
            vis = self.frame.copy()
#             vis = copy.deepcopy(self.frame)
#把图像从rgb转换为hsv颜色空间，色调 饱和度 亮度
            hsv = cv2.cvtColor(self.frame, cv2.COLOR_BGR2HSV)
#函数去除阈值，去除背景部分
            mask = cv2.inRange(hsv, np.array((0., 60., 32.)), np.array((255., 255., 255.)))
#             self.selection = 1

            if self.selection:
#                 x0, y0, x1, y1 = 220, 110, 358, 245
                x0, y0, x1, y1 = self.selection
                self.track_window = (x0, y0, x1-x0, y1-y0)
#                 hsv_roi = hsv[y0:y1, x0:x1]
#                 mask_roi = mask[y0:y1, x0:x1]
                hsv_roi = cv2. cvtColor(roi,cv2. COLOR_BGR2HSV)
                mask_roi = cv2.inRange(hsv_roi, np.array((0., 0., 0.)), np.array((255., 255., 255.)))
                #一维直方图参数设置
                #opencv HSV的H显示范围为0-180
                hist = cv2.calcHist( [hsv_roi], [0], mask_roi, [32], [0, 180] )
                #二维直方图参数设置
                #hist = cv2.calcHist( [hsv_roi], [0,2],None, [180,256], [0, 180,0 , 255] )
                #hist = cv2.calcHist( [hsv_roi], [0, 2],None, [180,256], [0, 180, 0, 255] )
#将绘制出来的直方图归一化，设定显示范围
                cv2.normalize(hist, hist, 0, 255, cv2.NORM_MINMAX);
                self.hist = hist.reshape(-1)
                #二维直方图显示
#               plt.imshow(hist,interpolation = 'nearest')
#               plt.show()
#		plt.imshow(hist,interpolation = 'nearest')
#               plt.show()
				#显示出直方图
                self.show_hist()

                vis_roi = vis[y0:y1, x0:x1]
#bitwise_not是对二进制数据进行“非”操作，即对图像（灰度图像或彩色图像均可）每个像素值进行二进制“非”操作，~1=0，~0=1
                cv2.bitwise_not(vis_roi, vis_roi)
                vis[mask == 0] = 0

            if self.tracking_state == 1:
                self.selection = None
#反向投影
#BackProjection中存储的数值代表了测试图像中该像素属于红色、蓝色区域的概率
#这里是图像函数，channel,掩模图像，像素值范围，（可选输出反向投影的比例因子） 像素是否均匀分布
                prob = cv2.calcBackProject([hsv], [0], self.hist, [0, 180], 1)
#与掩码相与，去除不需要的地方
                prob &= mask
#设置迭代的终止标准，最多十次迭代
                term_crit = ( cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 10, 1 )
                track_box, self.track_window = cv2.CamShift(prob, self.track_window, term_crit)
#                 if track_box[0][1] <= 240:
#             self.ser.write(str(int(track_box[0][0])-320) + " " + str(int(track_box[0][1])-240))
#             print str(int(track_box[0][0])-320) + " " + str(int(track_box[0][1])-240)
				##如果捕捉到的颜色区域高度小于等于1，则重新开始查找
                if track_box[1][1] <= 1:
                    self.tracking_state = 0
                    self.start()
                    print(track_box)
                    print("===================================================")
                else:
                    if self.show_backproj:
                        #将查询到的背景值赋给vis
                        vis[:] = prob[...,np.newaxis]
                    try:
                        #画为椭圆
                        cv2.ellipse(vis, track_box, (0, 0, 255), 2)
#                         print track_box
# 打印中心的x，y坐标；宽，高；旋转角度
                        a = str(track_box[0][0])+" "+str(track_box[0][1])+" "+str(round(track_box[1][0],2))\
                                       +" "+str(round(track_box[1][1],2))+" "+str(round(track_box[2],2))+"\r\n"
                        print(a)
#                         self.ser.write(a)
                    except: print(track_box)

            cv2.imshow('camshift', vis)

            ch = 0xFF & cv2.waitKey(5)
            if ch == 27:
                break
            if ch == ord('b'):
                #切换显示模式
                self.show_backproj = not self.show_backproj
            if ch == ord('r'):
                #重新捕捉颜色区域
                self.tracking_state = 0
                self.start()
        cv2.destroyAllWindows()


if __name__ == '__main__':
    import sys
    try: color = sys.argv[1]
    except: color = 1
    print(__doc__)
    a = App(color)
    a.run()

```

注意如果想要捕捉其他颜色的区域，需要加入读取图片后，更改tracking_state = 1。

以及涉及到运行程序时输入参数应将其转换为整形参数。

```python
int(color)
```

编译后，文件为

![1544438569407](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1544438569407.png)

运行效果如图所示

![1544438680557](C:\Users\pcmpc\AppData\Roaming\Typora\typora-user-images\1544438680557.png)