#include "Bridge_$$MODELER_NAME.h"

#include <core/jerboaRuleOperation.h>

// Embeddings includes

$$EBD_INCLUDE
//////////////////////


using namespace $$MODELER_NAMESPACE;


jerboa::JerboaEmbedding* Serializer_$$MODELER_NAME::unserialize(std::string ebdName, std::string valueSerialized)const{
    /** TODO: replace by your own embeddings **/
    if(valueSerialized=="nullptr") return nullptr;
    $$EBD_UNSERIALIZE
    else
        std::cerr << "No serialization found for " << ebdName << " please see class <Serializer_$$MODELER_NAME>" << std::endl;
    return nullptr;
}
std::string Serializer_$$MODELER_NAME::serialize(jerboa::JerboaEmbeddingInfo* ebdinf,jerboa::JerboaEmbedding* ebd)const{
    /** TODO: replace by your own embeddings **/
    if(ebd==nullptr) return "nullptr";
    $$EBD_SERIALIZE
    else{
        std::cerr << "No serialization found : please see class <Serializer_$$MODELER_NAME>" << std::endl;
        return "";
    }
}
std::string Serializer_$$MODELER_NAME::ebdClassName(jerboa::JerboaEmbeddingInfo* ebdinf)const{
    /** TODO: replace by your own embeddings **/
    if(ebdinf==nullptr){
        std::cerr << "No serialization found (null value for JerboaEmbeddingInfo) : please see class <Serializer_$$MODELER_NAME>" << std::endl;
        return "";
    }
    $$EBD_CLASS_NAME
    else{
        std::cerr << "No serialization found : please see class <Serializer_$$MODELER_NAME>" << std::endl;
        return "";
    }
}
int Serializer_$$MODELER_NAME::ebdId(std::string ebdName, jerboa::JerboaOrbit orbit)const{
    /** TODO: replace by your own embeddings **/
    $$EBD_ID
    return -1;
}
std::string Serializer_$$MODELER_NAME::positionEbd() const{
    /** TODO: replace by your own position Embedding name **/
    return "$$EBD_PRESET_POSITION_NAME";
}

std::string Serializer_$$MODELER_NAME::colorEbd()const{
    /** TODO: replace by your own color Embedding name **/
    return "$$EBD_PRESET_COLOR_NAME";
}
std::string Serializer_$$MODELER_NAME::orientEbd()const{
    /** TODO: replace by your own orient Embedding name **/
    return "$$EBD_PRESET_ORIENT_NAME";
}

bool Serializer_$$MODELER_NAME::getOrient(const JerboaDart* d)const{
    /** TODO: replace by your own orient value accessing **/
    return true;
}

JerboaEmbedding* Serializer_$$MODELER_NAME::unserializeBinary(const std::string& ebdName,
                                                           const ulong dataSize,
                                                           char* valueSerialized)const{
    /** TODO: replace by your own serializations **/
    if(dataSize>0){
        $$EBD_BINARY_UNSERIALIZE
        std::cerr << "No serialization found : please see class <Serializer_$$MODELER_NAME>" << std::endl;
    }
    return nullptr;
}
std::pair<ulong, char*> Serializer_$$MODELER_NAME::serializeBinary(JerboaEmbeddingInfo* ebdinf,
                                                                JerboaEmbedding* ebd)const{
    /** TODO: replace by your own serializations **/
    if(ebd!=nullptr){
    $$EBD_BINARY_SERIALIZE
        std::cerr << "No serialization found for '"  << ebdinf->name() << "' : please see class <Serializer_$$MODELER_NAME::serializeBinary>" << std::endl;
    }
    return std::pair<ulong, char*>(0,nullptr);
}


/** Bridge Functions **/

Bridge_$$MODELER_NAME::Bridge_$$MODELER_NAME():QObject(),
    jerboa::JerboaBridge(new modelerDemo::$$MODELER_NAME(),new Serializer_$$MODELER_NAME()){
    gmap=modeler->gmap();
    jm = nullptr;
    dynamic_cast<$$MODELER_NAME*>(modeler)->setBridge(this);
}
Bridge_$$MODELER_NAME::~Bridge_$$MODELER_NAME(){
    gmap=nullptr;
    jm = nullptr;
}


void Bridge_$$MODELER_NAME::setJm(JeMoViewer* jemo){
    if(jemo){
        dynamic_cast<$$MODELER_NAME*>(modeler)->setViewer(jemo);
        jm = jemo;
/*
        QMenuBar* optionModeleur = jm->menuBar();
        QMenu *optionModeleurMB = new QMenu("Option Modeler",optionModeleur);
        optionModeleur->addMenu(optionModeleurMB);

        QAction *computOrient = new QAction("Recompute orientation",optionModeleurMB);
        optionModeleurMB->addAction(computOrient);
        connect(computOrient, SIGNAL(triggered()), this, SLOT(recomputeOrient()));

        QAction *testOrient_but = new QAction("Test orientation",optionModeleurMB);
        optionModeleurMB->addAction(testOrient_but);
        connect(testOrient_but, SIGNAL(triggered()), this, SLOT(testOrient()));
*/
    }
}

bool Bridge_$$MODELER_NAME::coord(const JerboaDart* n, QVector3D& coordinate)const{
    /** TODO: replace by your own translation **/
    $$EBD_PRESET_POSITION_TYPE* v = dynamic_cast<$$EBD_PRESET_POSITION_TYPE*>(n->ebd($$EBD_PRESET_POSITION_ID));
    if(v==nullptr)return false;
    coordinate = QVector3D(v->x(),v->y(),v->z());
    return true;
}

std::string Bridge_$$MODELER_NAME::coordEbdName()const{
    /** TODO: replace by your own position ebd name **/
    return "$$EBD_PRESET_POSITION_NAME";
}

bool Bridge_$$MODELER_NAME::hasOrient()const{
    /** TODO: replace by your own value **/
    return true;
}

bool Bridge_$$MODELER_NAME::orient(const JerboaDart* n)const{
    /** TODO: replace by your own translation **/
    $$EBD_PRESET_ORIENT_TYPE* b = dynamic_cast<$$EBD_PRESET_ORIENT_TYPE*>(n->ebd($$EBD_PRESET_ORIENT_ID));
    if(b!=nullptr)
        //return false;
        return b->val();
    return false;
}

bool Bridge_$$MODELER_NAME::hasColor()const{
    /** TODO: replace by your own value **/
    return true;
}

bool Bridge_$$MODELER_NAME::color(const JerboaDart* n, QColor& color)const{
    /** TODO: replace by your own translation **/
    $$EBD_PRESET_COLOR_TYPE* c = dynamic_cast<$$EBD_PRESET_COLOR_TYPE*>(n->ebd($$EBD_PRESET_COLOR_ID));
    if(c==nullptr)return false;
    color = QColor(c->getR()*255,c->getG()*255,c->getB()*255,c->getA()*255);
    return true;
}

std::string Bridge_$$MODELER_NAME::colorEbdName()const{
    /** TODO: replace by your own color ebd name **/
    return "$$EBD_PRESET_COLOR_NAME";
}

bool Bridge_$$MODELER_NAME::hasNormal()const{
    /** TODO: replace by your own value **/
    return false;
}
bool Bridge_$$MODELER_NAME::normal(JerboaDart* n, QVector3D& normal)const{
    /** TODO: replace by your own normal computation **/
    if(hasNormal()){
        //return n->ebd("normalEBD");
    }else if(hasOrient()){
        // pour modèle chargé, on test juste si le plongement existe
        jerboa::JerboaDart* tmp = n;
        if(orient(n)){
            tmp = tmp->alpha(1);
        }
        QVector3D p0,p1,p2;
        if(coord(tmp,p0)){
            if(coord(tmp->alpha(0),p1)){
                if(coord(tmp->alpha(1)->alpha(0),p2)){
                    normal = QVector3D::crossProduct(p1-p0,p2-p0);
                    normal.normalize();
                    return true;
                }
            }
        }
        // If not worked
        jerboa::JerboaMark markview = gmap->getFreeMarker();
        while(tmp->isNotMarked(markview)){
            tmp->mark(markview);
            if(coord(tmp,p0)){
                if(coord(tmp->alpha(0),p1)){
                    if(coord(tmp->alpha(1)->alpha(0),p2)){
                        normal = QVector3D::crossProduct(p1-p0,p2-p0);
                        normal.normalize();
                        return true;
                    }
                }
            }
            tmp = tmp->alpha(0)->alpha(1);
        }
        gmap->freeMarker(markview);
    }
    return false;
}


std::string Bridge_$$MODELER_NAME::toString(jerboa::JerboaEmbedding* e)const{
	if(e!=nullptr){
    $$EBD_TO_STRING
   	}
    return "----";
}

jerboa::JerboaOrbit Bridge_$$MODELER_NAME::getEbdOrbit(const std::string& name)const{
    return modeler->getEmbedding(name)->orbit();
}


void Bridge_$$MODELER_NAME::extractInformationFromJBAFormat(std::string fileName){
    /** TODO: fill by your own supplement information reading process to add in JBA files **/
}
std::string Bridge_$$MODELER_NAME::getExtraInformationForJBAFormat(){
    /** TODO: fill by your own supplement information to add in JBA files **/
    return "";
}

void Bridge_$$MODELER_NAME::doCommand(const std::string& cmd){
    /*QRegularExpression applyRuleRegExp("@rule<([\\w]+)>([\\d]+)");
    QRegularExpressionMatchIterator match = applyRuleRegExp.globalMatch(QString::fromStdString(cmd), 0, QRegularExpression::NormalMatch);
    while (match.hasNext()) {
        QRegularExpressionMatch m = match.next();
        doSimulation(m.captured(1),m.captured(2).toInt());
    }


    QRegularExpression printRegExp("@print\\((.*)\\)");
    match = printRegExp.globalMatch(QString::fromStdString(cmd), 0, QRegularExpression::NormalMatch);
    while (match.hasNext()) {
        std::cout << match.next().captured(1).toStdString() << std::endl;
    }*/
}

std::vector<std::pair<std::string,std::string>> Bridge_$$MODELER_NAME::getCommandLineHelper(const std::string& cmd)const{
    std::vector<std::pair<std::string,std::string>> commandList;
    /*commandList.push_back("@rule<RULE_NAME>(DART_ID)");
    commandList.push_back("@print(MSG)");*/
    return commandList;
}


/*void Bridge_$$MODELER_NAME::recomputeOrient(){
    JerboaGMap* gmap = modeler->gmap();
    std::vector<std::pair<JerboaDart*,bool>> stack;
    JerboaMark markView =gmap->getFreeMarker();
    const int ebdId = (($$MODELER_NAME*)modeler)->getorient()->id();

    for(ulong i=0;i<gmap->length();i++){
        if(gmap->existNode(i)){
            JerboaDart* n = gmap->node(i);
            if(n->isNotMarked(markView) && !(n->ebd( (($$MODELER_NAME*)modeler)->getorient()->id()))){
                stack.push_back(std::pair<JerboaDart*,bool>(n,true));
                while(stack.size()>0){
                    std::pair<JerboaDart *,bool>ni = stack.back();
                    stack.pop_back();
                    if(ni.first->isNotMarked(markView)){
                        ni.first->mark(markView);
                        for(uint i=0;i<=modeler->dimension();i++){
                            stack.push_back(std::pair<JerboaDart*,bool>(ni.first->alpha(i),!ni.second));
                        }
                        if(ni.first->ebd(ebdId)!=nullptr)
                            (($EBD_PRESET_ORIENT_TYPE*)ni.first->ebd(ebdId))->setVal(ni.second);
                        else{
                            ni.first->setEbd(ebdId,gmap->addEbd(new $EBD_PRESET_ORIENT_TYPE(ni.second)));
                        }
                    }
                }
            }
        }
    }
    gmap->freeMarker(markView);
}*/

/*void Bridge_$$MODELER_NAME::testOrient(){
    bool test=true;
    for(uint i=0;i<jm->getModeler()->gmap()->length();i++){
        if(jm->getModeler()->gmap()->existNode(i)){
            jerboa::JerboaDart* ni = jm->getModeler()->gmap()->node(i);
            for(uint dim=0;dim<modeler->dimension();dim++){
                if(ni->alpha(dim)->id() != ni->id() && ni->ebd( (($$MODELER_NAME*)modeler)->getorient()->id()) == ni->alpha(dim)->ebd( (($$MODELER_NAME*)modeler)->getorient()->id())){
                    std::cerr << "wrong orientation : " << ni->id() << " -> " << ni->alpha(dim)->id() << std::endl;
                    std::cerr << ni->ebd( (($$MODELER_NAME*)modeler)->getorient()->id()) << " --  " << ni->alpha(dim)->ebd( (($$MODELER_NAME*)modeler)->getorient()->id()) << std::endl;
                    test = false;
                }
            }
        }
    }
    if(test)
        QMessageBox::information( jm, tr("Test orientation"), tr("Orientation is Correct") );
    else
        QMessageBox::critical(jm, tr("Test orientation"), tr("Orientation is not correct") );
}*/
