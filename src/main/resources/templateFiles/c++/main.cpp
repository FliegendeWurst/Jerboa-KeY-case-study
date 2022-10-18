/**
$$MAIN_HEADER
**/
#ifndef WIN32
#include <unistd.h>
#endif

// FOR JeMoViewer
#include <core/jemoviewer.h>
#include <QApplication>
#include <QStyle>
#include <QtWidgets>
/////////////////

#include <coreutils/chrono.h>
#include <serialization/serialization.h>
#include <serialization/defaultserializationhandler.h>
#include "Bridge_$$MODELER_NAME.h"

#include <coreutils/jerboagmaparray.h>


void printHelper(){
    std::cout << "### HELPER ###" << std::endl;
    std::cout << "-h \t\t: print this help " << std::endl;
    std::cout << "-l [FILENAME]\t: load a file " << std::endl;
    std::cout << "-e [FILENAME]\t: export result to file " << std::endl;
    std::cout << "-i {INTEGER}\t: specify how many time the rule must be applied " << std::endl;
    std::cout << "-r [RULENAME]\t: apply a rule" << std::endl;
    std::cout << "-a \t\t : list all rules" << std::endl;
}

using namespace jerboa;

int main(int argc, char *argv[]){
    bool COMMAND_LINE_MOD = true; // TODO: change this value as you wish


    srand(time(nullptr));

    if(COMMAND_LINE_MOD){

        Chrono cr;
        Bridge_$$MODELER_NAME bridge;
        jerboa::JerboaModeler* modeler = bridge.getModeler();
        jerboa::JerboaGMap* gmap = modeler->gmap();

        bool exportFile = false;
        std::string fileImportName="", fileExportName="";
        std::string ruleName ="";
        int iteration=0;
        DefaultSerializationHandler serial;

    #ifdef __unix

        int c;

        opterr = 0;
        while ((c = getopt (argc, argv, "hai:l:e:r:")) != -1)
            switch (c)
            {
            case 'i':
                //            std::cout << optarg << std::endl;
                iteration = atoi(optarg);
                break;
            case 'a':
                std::cout << " ######## "<< std::endl;
                for(jerboa::JerboaRuleOperation* r: modeler->allRules()){
                    std::cout << r->name() << std::endl;
                }
                std::cout << " ######## "<< std::endl;
                break;
            case 'h':
                printHelper();
                break;
            case 'l':
                fileImportName = strdup(optarg);
                std::cout << "import >>>> " << fileImportName << std::endl;
                serial.loadModel(fileImportName,&bridge);
                break;
            case 'e':
                exportFile = true;
                fileExportName =  strdup(optarg);
                break;
            case 'r':
                ruleName = strdup(optarg);
                break;
            case '?':
                if (optopt == 'c')
                    fprintf (stderr, "Option -%c requires an argument.\n", optopt);
                else if (isprint (optopt))
                    fprintf (stderr, "Unknown option `-%c'.\n", optopt);
                else
                    fprintf (stderr,
                             "Unknown option character `\\x%x'.\n",
                             optopt);
                return 1;
            default:
                abort ();
            }

    #else // windows
        std::cout << "### HELPER ###" << std::endl;
        std::cout << "./app [RULENAME] {ITER} [Input_FILENAME] [Output_FILENAME]" << std::endl;
        if(argc>1){
            ruleName = argv[1];
        }else{
            std::cout << " ######## "<< std::endl;
            for(jerboa::JerboaRuleOperation* r: modeler->allRules()){
                std::cout << r->name() << std::endl;
            }
            std::cout << " ######## "<< std::endl;
        }
        if(argc>2){
            iteration = atoi(argv[2]);
        }
        if(argc>3){
            fileImportName = strdup(argv[3]);
            std::cout << "import >>>> " << fileImportName << std::endl;
            serial.loadModel(fileImportName,&bridge);
        }
        if(argc>4){
            exportFile=true;
            fileExportName = strdup(argv[4]);
        }
    #endif
        /*if(ruleName.size()<=0){
            ruleName = "CatmullClark";
            iteration = 3;
            serial.loadModel("/YOUR/PATH/TO/A/CUBE/FILE/cube.moka",&bridge);
        }*/

        std::cout << "# application of rule '"  << ruleName << "' iterations : " << iteration
                  << " export name is : " << fileExportName
                  << " import name is : " << fileImportName << std::endl;


        if(ruleName.size()>0){

            jerboa::JerboaRuleOperation* rop = modeler->rule(ruleName);
            if(!rop){
                std::cerr << "rule not found '" <<ruleName<<"'"<<std::endl;
                return EXIT_FAILURE;
            }
            jerboa::JerboaInputHooksGeneric hook;
            if(rop->left().size()>0){
                jerboa::JerboaDart* n = modeler->gmap()->node(0);
                hook.addRow(0,n);
            }
            cr.start();
            for(int i=0;i<iteration;i++){
                rop->applyRule(gmap,hook);
            }
            cr.stop();
            std::cout << "Gmap length : " << gmap->length()
                      << " Gmap size : " << gmap->size() << std::endl;
            std::cout << "Elapsed Time(us) : " << cr.us() << std::endl;
            std::cout << "Elapsed Time(ms) : " << cr.ms() << std::endl;

            rop = nullptr;
        }
        if(exportFile){
            std::cout << "export name is : " << fileExportName << std::endl;
            serial.saveModel(fileExportName,&bridge, true);
        }

        modeler = nullptr;
        gmap    = nullptr;


        return EXIT_SUCCESS;
    
    }else{ // 3D Visualization mod

    printf("Compiled with Qt Version %s", QT_VERSION_STR);
    QApplication app(argc, argv);
 	Bridge_$$MODELER_NAME bridge;
    JeMoViewer w(bridge.getModeler(),&bridge, &app);
    bridge.setJm(&w);
    app.setStyle(QStyleFactory::create(QStyleFactory::keys()[QStyleFactory::keys().size()-1]));
    app.setFont(QFont("Century",9));
    #ifndef WIN32
        QString versionString(QLatin1String(reinterpret_cast<const char*>(glGetString(GL_VERSION))));
        qDebug() << "Driver Version String:" << versionString;
        qDebug() << "Current Context:" << w.gmapViewer()->format();
    #endif


        w.show();
        for(int i=1;i<argc;i++){
            w.loadModel(argv[i]);
        }
        int resApp = app.exec();
        return resApp;
    }
}
