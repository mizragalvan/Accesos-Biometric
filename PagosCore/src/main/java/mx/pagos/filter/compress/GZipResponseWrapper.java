package mx.pagos.filter.compress;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class GZipResponseWrapper extends HttpServletResponseWrapper {

	  private GZipOutputStream gzipOutputStream = null;
	  private PrintWriter             printWriter      = null;

	  public GZipResponseWrapper(HttpServletResponse response)
	          throws IOException {
	      super(response);
	  }

	  public void close() throws IOException {
	      if (this.printWriter != null) {
	          this.printWriter.close();
	      }

	      if (this.gzipOutputStream != null) {
	          this.gzipOutputStream.close();
	      }
	  }

	  @Override
	  public void flushBuffer() throws IOException {
	    if(this.printWriter != null) {
	      this.printWriter.flush();
	    }

	    IOException exception1 = null;
	    try{
	      if(this.gzipOutputStream != null) {
	        this.gzipOutputStream.flush();
	      }
	    } catch(IOException e) {
	        exception1 = e;
	    }

	    IOException exception2 = null;
	    try {
	      super.flushBuffer();
	    } catch(IOException e){
	      exception2 = e;
	    }

	    if(exception1 != null) throw exception1;
	    if(exception2 != null) throw exception2;
	  }

	  @Override
	  public ServletOutputStream getOutputStream() throws IOException {
	    if (this.printWriter != null) {
	      throw new IllegalStateException(  "Error generando el Writer de Salida");
	    }
	    if (this.gzipOutputStream == null) {
	      this.gzipOutputStream = new GZipOutputStream(
	        getResponse().getOutputStream());
	    }
	    return this.gzipOutputStream;
	  }

	  @Override
	  public PrintWriter getWriter() throws IOException {
	     if (this.printWriter == null && this.gzipOutputStream != null) {
	       throw new IllegalStateException( "Error generando el Writer de Salida");
	     }
	     if (this.printWriter == null) {
	       this.gzipOutputStream = new GZipOutputStream(getResponse().getOutputStream());
	       this.printWriter      = new PrintWriter(new OutputStreamWriter(
	       this.gzipOutputStream, getResponse().getCharacterEncoding()));
	     }
	     return this.printWriter;
	  }


	  @Override
	  public void setContentLength(int len) {
	  }
	}
