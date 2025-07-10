# Directorios
SRC_DIR = project
BIN_DIR = bin
LIB_DIR = libjar

# Configuración del classpath
CLASSPATH = .:$(LIB_DIR)/*

# Archivos fuente y clases
SRCS = $(wildcard $(SRC_DIR)/*.java)
CLASSES = $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(SRCS))

# Nombre del archivo ejecutable
JAR_FILE = apiOracle.jar
RUN_FILE = apiOracle.run

.PHONY: all clean run

all: $(RUN_FILE)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(BIN_DIR)
	javac -cp $(CLASSPATH) -d $(BIN_DIR) $<

$(JAR_FILE): $(CLASSES)
	jar -cfvm $(JAR_FILE) MANIFEST.MF -C $(BIN_DIR) .

$(RUN_FILE): $(JAR_FILE)
	cat stub.sh $(JAR_FILE) > $(RUN_FILE) && chmod +x $(RUN_FILE)
	chmod 777 $(JAR_FILE)
	chmod 777 $(RUN_FILE)

run: $(RUN_FILE)
	clear
	./$(RUN_FILE) -p 9000

clean:
	rm -rf $(BIN_DIR)
	rm -f $(JAR_FILE) $(RUN_FILE)