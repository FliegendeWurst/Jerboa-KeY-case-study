$$PRO_HEADER

include($$PWD/$$PRO_MODELER_NAME.pri)

QT += widgets opengl concurrent core

unix:!macx {
    QMAKE_CXXFLAGS += -std=c++11  -Wno-unused-parameter -Wno-unused-variable -Wno-sign-compare
    QMAKE_CXXFLAGS += -fopenmp
    LIBS += -fopenmp
}
win32-g++ {
    QMAKE_CXXFLAGS += -std=c++11  -Wno-unused-parameter -Wno-unused-variable -Wno-sign-compare
    QMAKE_CXXFLAGS += -fopenmp
    LIBS += -fopenmp
}
win32-msvc {
    QMAKE_CXXFLAGS += /std:c++latest #/openmp
#    LIBS += /openmp
}


TARGET = $$PRO_MODELER_NAME
TEMPLATE =  app

INCLUDEPATH += $$PWD/

ARCH = "_86"
contains(QT_ARCH, i386) {
	message("compilation for 32-bit")
}else{
	message("compilation for 64-bit")
	ARCH ="_64"
}

INCLUDE = $$PWD/include
BIN     = $$PWD/bin
BUILD   = $$PWD/build
SRC     = $$PWD/src

CONFIG(debug, debug|release) {
	DESTDIR = $$BIN/debug$$ARCH
	OBJECTS_DIR = $$BUILD/debug$$ARCH/.obj
	MOC_DIR = $$BUILD/debug$$ARCH/.moc
	RCC_DIR = $$BUILD/debug$$ARCH/.rcc
	UI_DIR = $$BUILD/debug$$ARCH/.ui
	OBJECTS_DIR = $$BUILD/debug$$ARCH/object
} else {
	DESTDIR = $$BIN/release$$ARCH
	OBJECTS_DIR = $$BUILD/release$$ARCH/.obj
	MOC_DIR = $$BUILD/release$$ARCH/.moc
	RCC_DIR = $$BUILD/release$$ARCH/.rcc
	UI_DIR = $$BUILD/release$$ARCH/.ui
	OBJECTS_DIR = $$BUILD/release$$ARCH/object
}

SOURCES += main$$PRO_MODELER_NAME.cpp\
	Bridge_$$PRO_MODELER_NAME.cpp

HEADERS +=\
	Bridge_$$PRO_MODELER_NAME.h

##############  JERBOA library
JERBOADIR = $$PWD/../../Jerboa++
JERBOALIBDIR = $$JERBOADIR/lib/debug$$ARCH
if(CONFIG(release, debug|release)){
    JERBOALIBDIR = $$JERBOADIR/lib/release$$ARCH
}
LIBS += -L$$JERBOALIBDIR -lJerboa
message("Jerboa lib is taken in : " + $$JERBOALIBDIR)

INCLUDEPATH += $$JERBOADIR/include
DEPENDPATH += $$JERBOADIR/include

# JeMoViewer library
message("TODO: Change JERBOA_MODELER_VIEWER_SRC_PATH in pro file")
JERBOA_MODELER_VIEWER_SRC_PATH = $$PWD/../../JeMoViewer
JERBOA_MODELER_VIEWERPATH = $$JERBOA_MODELER_VIEWER_SRC_PATH/lib/debug$$ARCH
if(CONFIG(release, debug|release)){
    JERBOA_MODELER_VIEWERPATH = $$JERBOA_MODELER_VIEWER_SRC_PATH/lib/release$$ARCH
}
LIBS += -L$$JERBOA_MODELER_VIEWERPATH -lJeMoViewer
message("JeMoViewer lib is taken in : " + $$JERBOA_MODELER_VIEWERPATH)

INCLUDEPATH += $$JERBOA_MODELER_VIEWER_SRC_PATH/include
DEPENDPATH += $$JERBOA_MODELER_VIEWER_SRC_PATH/include

win32{ RC_FILE = $$JERBOA_MODELER_VIEWER_SRC_PATH/rc_icon_win.rc }
unix:!macx{}
macx{ ICON = $$JERBOA_MODELER_VIEWER_SRC_PATH/images.jerboaIcon.ics }



## Embeddings

HEADERS +=\
        $$JERBOADIR/include/embedding/vec3.h\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/include/embedding/vector.h\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/include/embedding/colorV.h\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/include/embedding/booleanV.h\



SOURCES += \
        $$JERBOADIR/src/embedding/vec3.cpp\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/src/embedding/vector.cpp\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/src/embedding/colorV.cpp\
        $$JERBOA_MODELER_VIEWER_SRC_PATH/src/embedding/booleanV.cpp\

