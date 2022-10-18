#ifndef JERBOA_Bridge_$$MODELER_NAME
#define JERBOA_Bridge_$$MODELER_NAME

#include <core/jemoviewer.h>
#include <core/jerboamodeler.h>
#include <serialization/serialization.h>
#include <core/jerboadart.h>

#include "$$MODELER_PATH.h"

using namespace jerboa;

class Serializer_$$MODELER_NAME : public jerboa::EmbeddginSerializer{
public :
    Serializer_$$MODELER_NAME(){}
    ~Serializer_$$MODELER_NAME(){}

    bool getOrient(const JerboaDart* d)const;

    // Inherited
    JerboaEmbedding* unserialize(std::string ebdName, std::string valueSerialized)const;
    std::string serialize(JerboaEmbeddingInfo* ebdinf,JerboaEmbedding* ebd)const;

    std::string ebdClassName(JerboaEmbeddingInfo* ebdinf)const;
    int ebdId(std::string ebdName, JerboaOrbit orbit)const;

    std::string positionEbd()const;
    std::string colorEbd()const;
    std::string orientEbd()const;
    JerboaEmbedding* unserializeBinary(const std::string& ebdName,
                                       const ulong dataSize,
                                       char* valueSerialized)const;
    std::pair<ulong, char*> serializeBinary(JerboaEmbeddingInfo* ebdinf,
                                            JerboaEmbedding* ebd)const;


}; // end Class


class Bridge_$$MODELER_NAME: public QObject, public jerboa::JerboaBridge{
    Q_OBJECT
private:
    jerboa::JerboaGMap* gmap;

    JeMoViewer* jm;
public:
	Bridge_$$MODELER_NAME();
	~Bridge_$$MODELER_NAME();

    void setJm(JeMoViewer* jemo);

    // Inherited
    bool coord(const JerboaDart* n, QVector3D& coordinate)const;
    std::string coordEbdName()const;

    bool hasColor()const;
    bool color(const JerboaDart* n, QColor& color)const;
    std::string colorEbdName()const;

    bool hasOrient()const;
    bool orient(const JerboaDart* n)const;

    bool hasNormal()const;
    bool normal(JerboaDart* n, QVector3D& normal)const;

    std::string toString(JerboaEmbedding* e)const;
    JerboaOrbit getEbdOrbit(const std::string& name)const;

    void extractInformationFromJBAFormat(std::string fileName);
    std::string getExtraInformationForJBAFormat();

    void doCommand(const std::string& cmd);
    std::vector<std::pair<std::string,std::string>> getCommandLineHelper(const std::string& cmd)const;


};// end Class


#endif
